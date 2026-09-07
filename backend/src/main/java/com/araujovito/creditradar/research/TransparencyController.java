package com.araujovito.creditradar.research;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/analytics/transparency") public class TransparencyController {
 /** Eixos que se repetem no tempo têm cadência; os que descrevem a operação uma vez, não. */
 private static final List<Axis> AXES=List.of(
  new Axis("debtor","Devedora","Dá para nomear a devedora e o CNPJ em documento primário?",false),
  new Axis("term","Termo","O termo de securitização está em área aberta?",false),
  new Axis("guarantees","Garantias","As garantias estão descritas em documento primário, e não só listadas em cadastro?",false),
  new Axis("coverage","Cobertura","Há apuração de LTV ou índice de cobertura publicada, recente e na periodicidade exigida?",true),
  new Axis("financials","Demonstrações","Há demonstrações financeiras da devedora, do exercício mais recente?",true),
  new Axis("monitoring","Acompanhamento","Há acompanhamento periódico público, recente e regular?",true));

 private final TransparencyScoreRepository scores;
 private final AxisCadenceRepository cadences;
 public TransparencyController(TransparencyScoreRepository scores,AxisCadenceRepository cadences){this.scores=scores;this.cadences=cadences;}

 @GetMapping public Report report(){
  var today=LocalDate.now();
  var rows=scores.findAll().stream().sorted(Comparator.comparing(TransparencyScore::getAmountMillions).reversed()).toList();
  if(rows.isEmpty())return new Report(AXES,List.of(),null);

  var bySlug=new HashMap<String,Map<String,AxisCadence>>();
  for(var cadence:cadences.findAll())
   bySlug.computeIfAbsent(cadence.getSlug(),key->new HashMap<>()).put(cadence.getAxis(),cadence);

  var ops=new ArrayList<Operation>();
  for(var row:rows){
   var perAxis=bySlug.getOrDefault(row.getSlug(),Map.of());
   var levels=new LinkedHashMap<String,Integer>();
   var detail=new LinkedHashMap<String,Cadence>();
   var points=0;
   for(var axis:AXES){
    var raw=row.rawLevel(axis.key());
    var cadence=perAxis.get(axis.key());
    var effective=cadence!=null&&raw>1&&cadence.capsToPartial(today)?1:raw;
    levels.put(axis.key(),effective);
    points+=effective;
    if(cadence!=null)detail.put(axis.key(),describe(cadence,today));
   }
   ops.add(new Operation(row.getSlug(),row.getOperationName(),row.getCreditCode(),row.getAmountMillions(),
     levels,detail,points,TransparencyScore.percentOf(points),row.getGapNote()));
  }

  var total=rows.stream().map(TransparencyScore::getAmountMillions).reduce(BigDecimal.ZERO,BigDecimal::add);
  var weighted=ops.stream().map(op->op.amountMillions().multiply(BigDecimal.valueOf(op.points())))
    .reduce(BigDecimal.ZERO,BigDecimal::add)
    .multiply(BigDecimal.valueOf(100)).divide(total.multiply(BigDecimal.valueOf(TransparencyScore.MAX)),0,RoundingMode.HALF_UP);
  var simple=BigDecimal.valueOf(ops.stream().mapToInt(Operation::points).sum()*100L)
    .divide(BigDecimal.valueOf((long)ops.size()*TransparencyScore.MAX),0,RoundingMode.HALF_UP);

  var byAxis=new LinkedHashMap<String,AxisSummary>();
  for(var axis:AXES){
   if(!axis.recurring())continue;
   var measured=cadences.findAll().stream().filter(c->c.getAxis().equals(axis.key())&&c.isMeasured()).toList();
   var expected=measured.stream().mapToInt(AxisCadence::getExpectedPerYear).sum();
   var published=measured.stream().mapToInt(AxisCadence::getPublished12m).sum();
   byAxis.put(axis.key(),new AxisSummary(axis.label(),measured.size(),rows.size()-measured.size(),published,expected,
     expected==0?null:pct((double)published/expected)));
  }

  var portfolio=new Portfolio(rows.size(),total,simple,weighted,byAxis,
    exposure(ops,op->op.levels().get("debtor")==0,total),
    exposure(ops,op->op.levels().get("coverage")>0,total),
    exposure(ops,op->op.levels().get("term")==0,total));
  return new Report(AXES,ops,portfolio);
 }

 private Cadence describe(AxisCadence cadence,LocalDate today){
  var regularity=cadence.getRegularity();
  return new Cadence(cadence.getLatestReference(),cadence.getAgeMonths(today),cadence.isStale(today),cadence.getStaleMonths(),
    cadence.getPublished12m(),cadence.getExpectedPerYear(),regularity==null?null:pct(regularity),
    cadence.isIrregular(),cadence.isMeasured(),cadence.getBasis());
 }

 private Exposure exposure(List<Operation> ops,java.util.function.Predicate<Operation> match,BigDecimal total){
  var picked=ops.stream().filter(match).toList();
  var amount=picked.stream().map(Operation::amountMillions).reduce(BigDecimal.ZERO,BigDecimal::add);
  var percent=total.signum()==0?BigDecimal.ZERO:amount.multiply(BigDecimal.valueOf(100)).divide(total,0,RoundingMode.HALF_UP);
  return new Exposure(picked.size(),amount,percent,picked.stream().map(Operation::operationName).toList());
 }

 private static BigDecimal pct(double value){return BigDecimal.valueOf(value*100).setScale(0,RoundingMode.HALF_UP);}

 public record Axis(String key,String label,String question,boolean recurring){}
 public record Cadence(LocalDate latestReference,Long ageMonths,boolean stale,int staleMonths,
   Integer published12m,Integer expectedPerYear,BigDecimal regularity,boolean irregular,boolean measured,String basis){}
 public record Operation(String slug,String operationName,String creditCode,BigDecimal amountMillions,
   Map<String,Integer> levels,Map<String,Cadence> cadence,int points,BigDecimal percent,String gapNote){}
 public record AxisSummary(String label,int measured,int unmeasured,int published12m,int expected12m,BigDecimal regularity){}
 public record Exposure(int operations,BigDecimal amountMillions,BigDecimal percent,List<String> names){}
 public record Portfolio(int operations,BigDecimal amountMillions,BigDecimal simpleScore,BigDecimal weightedScore,
   Map<String,AxisSummary> byAxis,Exposure withoutDebtor,Exposure withCoverage,Exposure withoutTerm){}
 public record Report(List<Axis> axes,List<Operation> operations,Portfolio portfolio){}
}
