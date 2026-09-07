package com.araujovito.creditradar.research;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import org.springframework.web.bind.annotation.*;

/** Prioriza diligência documental; não é recomendação de investimento nem rating de crédito. */
@RestController
@RequestMapping("/api/analytics/credit-risk")
public class CreditRiskController {
 private final DebtorDossierRepository dossiers;
 private final TransparencyScoreRepository transparency;
 private final CoverageMeasurementRepository coverages;
 public CreditRiskController(DebtorDossierRepository dossiers,TransparencyScoreRepository transparency,CoverageMeasurementRepository coverages){this.dossiers=dossiers;this.transparency=transparency;this.coverages=coverages;}

 @GetMapping public Report report(){
  var amounts=new HashMap<String,BigDecimal>(); for(var score:transparency.findAll()) amounts.put(score.getSlug(),score.getAmountMillions());
  var total=amounts.values().stream().reduce(BigDecimal.ZERO,BigDecimal::add); var rows=new ArrayList<Row>();
  for(var d:dossiers.findAll()){
   var reasons=new ArrayList<String>(); int points=0; var collateral=Optional.ofNullable(d.getCollateralStatus()).orElse("SEM INFORMAÇÃO");
   if(collateral.equals("SOB WAIVER")){points+=50;reasons.add("índice ou obrigação de garantia sob waiver");}
   else if(collateral.equals("PENDÊNCIAS ABERTAS")){points+=45;reasons.add("pendências abertas de razão ou formalização de garantia");}
   else if(collateral.equals("SEM INFORMAÇÃO")){points+=30;reasons.add("sem apuração pública de garantia");}
   else if(collateral.equals("CALCULADA AQUI")){points+=20;reasons.add("cobertura estimada pelo projeto, sem validação do agente fiduciário");}
   if("INADIMPLENTE".equals(d.getPaymentStatus())){points+=40;reasons.add("inadimplemento registrado pelo agente fiduciário");}
   else if(!"ADIMPLENTE".equals(d.getPaymentStatus())){points+=5;reasons.add("status de pagamento sem confirmação direta");}
   if(d.getLegalName().startsWith("A confirmar")||d.getLegalName().equals("Pesquisa não iniciada")||d.getCnpj().startsWith("A confirmar")){points+=15;reasons.add(d.getLegalName().equals("Pesquisa não iniciada")?"diligência documental ainda não iniciada":"devedora jurídica ou CNPJ não identificados");}
   var amount=amounts.getOrDefault(d.getSlug(),BigDecimal.ZERO); if(amount.compareTo(BigDecimal.valueOf(100))>=0){points+=10;reasons.add("exposição material acima de R$ 100 mi");}
   var coverage=coverages.findBySlug(d.getSlug()).orElse(null); if(coverage!=null&&coverage.getCoveragePercent().compareTo(BigDecimal.valueOf(100))<0){points+=25;reasons.add("cobertura calculada abaixo de 100%");}
   var priority=points>=60?"CRÍTICA":points>=35?"ALTA":points>=20?"MÉDIA":"MONITORAR";
   rows.add(new Row(d.getSlug(),d.getOperationName(),amount,percent(amount,total),points,priority,d.getPaymentStatus(),collateral,coverage==null?null:coverage.getCoveragePercent(),reasons));
  }
  rows.sort(Comparator.comparingInt(Row::points).reversed().thenComparing(Row::amountMillions,Comparator.reverseOrder()));
  var critical=rows.stream().filter(r->r.priority.equals("CRÍTICA")).toList(); var high=rows.stream().filter(r->r.priority.equals("ALTA")).toList();
  return new Report(rows,total,exposure(critical,total),exposure(high,total));
 }
 private Exposure exposure(List<Row> rows,BigDecimal total){var amount=rows.stream().map(Row::amountMillions).reduce(BigDecimal.ZERO,BigDecimal::add);return new Exposure(rows.size(),amount,percent(amount,total));}
 private BigDecimal percent(BigDecimal amount,BigDecimal total){return total.signum()==0?BigDecimal.ZERO:amount.multiply(BigDecimal.valueOf(100)).divide(total,0,RoundingMode.HALF_UP);}
 public record Row(String slug,String operationName,BigDecimal amountMillions,BigDecimal portfolioPercent,int points,String priority,String paymentStatus,String collateralStatus,BigDecimal coveragePercent,List<String> reasons){}
 public record Exposure(int operations,BigDecimal amountMillions,BigDecimal portfolioPercent){}
 public record Report(List<Row> operations,BigDecimal amountMillions,Exposure critical,Exposure high){}
}
