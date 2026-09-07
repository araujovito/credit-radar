package com.araujovito.creditradar.research;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/analytics/transparency") public class TransparencyController {
 private static final List<Axis> AXES=List.of(
  new Axis("debtor","Devedora","Dá para nomear a devedora e o CNPJ em documento primário?"),
  new Axis("term","Termo","O termo de securitização está em área aberta?"),
  new Axis("guarantees","Garantias","As garantias estão descritas em documento primário, e não só listadas em cadastro?"),
  new Axis("coverage","Cobertura","Há LTV ou índice de cobertura publicado, e recente, para aferir a garantia? Uma apuração com mais de 18 meses vale como parcial."),
  new Axis("financials","Demonstrações","Há demonstrações financeiras da devedora?"),
  new Axis("monitoring","Acompanhamento","Há relatório ou informe periódico público da operação?"));
 private final TransparencyScoreRepository scores;
 public TransparencyController(TransparencyScoreRepository scores){this.scores=scores;}

 @GetMapping public Report report(){
  var today=LocalDate.now();
  var rows=scores.findAll().stream().sorted(Comparator.comparing(TransparencyScore::getAmountMillions).reversed()).toList();
  if(rows.isEmpty())return new Report(AXES,List.of(),null);
  var total=rows.stream().map(TransparencyScore::getAmountMillions).reduce(BigDecimal.ZERO,BigDecimal::add);
  var ops=rows.stream().map(s->new Operation(s.getSlug(),s.getOperationName(),s.getCreditCode(),s.getAmountMillions(),
    Map.of("debtor",s.getDebtor(),"term",s.getTerm(),"guarantees",s.getGuarantees(),"coverage",s.getEffectiveCoverage(today),"financials",s.getFinancials(),"monitoring",s.getMonitoring()),
    s.getPoints(today),s.getPercent(today),s.getGapNote(),s.getCoverageAsOf(),s.getCoverageAgeMonths(today),s.isCoverageStale(today))).toList();
  var weighted=rows.stream().map(s->s.getAmountMillions().multiply(BigDecimal.valueOf(s.getPoints(today))))
    .reduce(BigDecimal.ZERO,BigDecimal::add)
    .multiply(BigDecimal.valueOf(100)).divide(total.multiply(BigDecimal.valueOf(TransparencyScore.MAX)),0,RoundingMode.HALF_UP);
  var simple=BigDecimal.valueOf(rows.stream().mapToInt(s->s.getPoints(today)).sum()*100L)
    .divide(BigDecimal.valueOf((long)rows.size()*TransparencyScore.MAX),0,RoundingMode.HALF_UP);
  var portfolio=new Portfolio(rows.size(),total,simple,weighted,
    exposure(rows,s->s.getDebtor()==0,total),exposure(rows,s->s.getCoverage()>0,total),exposure(rows,s->s.getTerm()==0,total),exposure(rows,s->s.isCoverageStale(today),total));
  return new Report(AXES,ops,portfolio);
 }

 private Exposure exposure(List<TransparencyScore> rows,java.util.function.Predicate<TransparencyScore> match,BigDecimal total){
  var picked=rows.stream().filter(match).toList();
  var amount=picked.stream().map(TransparencyScore::getAmountMillions).reduce(BigDecimal.ZERO,BigDecimal::add);
  var percent=total.signum()==0?BigDecimal.ZERO:amount.multiply(BigDecimal.valueOf(100)).divide(total,0,RoundingMode.HALF_UP);
  return new Exposure(picked.size(),amount,percent,picked.stream().map(TransparencyScore::getOperationName).toList());
 }

 public record Axis(String key,String label,String question){}
 public record Operation(String slug,String operationName,String creditCode,BigDecimal amountMillions,Map<String,Integer> levels,int points,BigDecimal percent,String gapNote,LocalDate coverageAsOf,Long coverageAgeMonths,boolean coverageStale){}
 public record Exposure(int operations,BigDecimal amountMillions,BigDecimal percent,List<String> names){}
 public record Portfolio(int operations,BigDecimal amountMillions,BigDecimal simpleScore,BigDecimal weightedScore,Exposure withoutDebtor,Exposure withCoverage,Exposure withoutTerm,Exposure staleCoverage){}
 public record Report(List<Axis> axes,List<Operation> operations,Portfolio portfolio){}
}
