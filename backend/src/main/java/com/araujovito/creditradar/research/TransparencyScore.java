package com.araujovito.creditradar.research;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Mede o que é possível saber sobre uma operação em fonte aberta — não a qualidade do crédito.
 * Cada eixo vale 2 (documento primário público), 1 (informação parcial ou de segunda mão)
 * ou 0 (ausente), de modo que o máximo por operação é 12.
 *
 * <p>Dois eixos têm prazo de validade. Documento que descrevia a operação e não a descreve mais
 * não é transparência corrente: vale como evidência parcial, nunca como verificação. O prazo
 * acompanha a periodicidade esperada de cada um — a cobertura é apurada em ciclos longos, o
 * acompanhamento é obrigação mensal.
 */
@Entity
public class TransparencyScore {
 public static final int MAX = 12;
 /** Apuração de LTV ou índice de cobertura mais velha que isto vale como parcial. */
 public static final int COVERAGE_STALE_MONTHS = 18;
 /** Informe ou relatório periódico mais velho que isto deixa de ser acompanhamento corrente. */
 public static final int MONITORING_STALE_MONTHS = 6;
 /** Informe mensal de CRI é obrigação mensal (Suplemento E da Resolução CVM 60). */
 public static final int MONITORING_EXPECTED_PER_YEAR = 12;
 /** Abaixo desta fração do esperado, publicar é evento isolado e não regime de acompanhamento. */
 public static final double MONITORING_MIN_REGULARITY = 0.5;

 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(nullable=false,unique=true) private String slug;
 @Column(nullable=false) private String operationName;
 @Column(nullable=false) private String creditCode;
 @Column(nullable=false,precision=12,scale=2) private BigDecimal amountMillions;
 private int debtor;      // devedora identificável por nome jurídico e CNPJ
 private int term;        // termo de securitização em área aberta
 private int guarantees;  // garantias descritas em documento primário
 private int coverage;    // LTV ou índice de cobertura publicado
 private int financials;  // demonstrações financeiras da devedora
 private int monitoring;  // acompanhamento periódico público
 @Column(nullable=false,length=1400) private String gapNote;
 /** Data-base da apuração de cobertura mais recente em fonte aberta; nulo quando não há. */
 private LocalDate coverageAsOf;
 /** Data de referência do relatório ou informe periódico mais recente; nulo quando não há. */
 private LocalDate monitoringAsOf;
 /**
  * Competências distintas publicadas em área aberta nos últimos doze meses.
  * Nulo significa não medido, o que é diferente de zero publicações — e por isso a coluna é
  * anulável: um int primitivo geraria NOT NULL, que o Hibernate não consegue aplicar a uma
  * tabela que já tem linhas.
  */
 private Integer monitoringPublished12m;

 protected TransparencyScore(){}
 public TransparencyScore(String slug,String operationName,String creditCode,BigDecimal amountMillions,int debtor,int term,int guarantees,int coverage,int financials,int monitoring,String gapNote,LocalDate coverageAsOf,LocalDate monitoringAsOf,Integer monitoringPublished12m){
  this.slug=slug;this.operationName=operationName;this.creditCode=creditCode;this.amountMillions=amountMillions;
  this.debtor=debtor;this.term=term;this.guarantees=guarantees;this.coverage=coverage;this.financials=financials;this.monitoring=monitoring;
  this.gapNote=gapNote;this.coverageAsOf=coverageAsOf;this.monitoringAsOf=monitoringAsOf;this.monitoringPublished12m=monitoringPublished12m;}

 public LocalDate getCoverageAsOf(){return coverageAsOf;}
 public LocalDate getMonitoringAsOf(){return monitoringAsOf;}
 public Long getCoverageAgeMonths(LocalDate today){return age(coverageAsOf,today);}
 public Long getMonitoringAgeMonths(LocalDate today){return age(monitoringAsOf,today);}
 public boolean isCoverageStale(LocalDate today){return stale(coverageAsOf,today,COVERAGE_STALE_MONTHS);}
 public boolean isMonitoringStale(LocalDate today){return stale(monitoringAsOf,today,MONITORING_STALE_MONTHS);}
 public int getEffectiveCoverage(LocalDate today){return coverage>1&&isCoverageStale(today)?1:coverage;}
 public Integer getMonitoringPublished12m(){return monitoringPublished12m;}
 public int getMonitoringExpected12m(){return MONITORING_EXPECTED_PER_YEAR;}
 public boolean isMonitoringMeasured(){return monitoringPublished12m!=null;}
 /** Fração do esperado efetivamente publicada nos últimos doze meses; nulo quando não medido. */
 public Double getMonitoringRegularity(){return monitoringPublished12m==null?null:(double)monitoringPublished12m/MONITORING_EXPECTED_PER_YEAR;}
 /**
  * Idade não é frequência. Quem publicou uma vez em dezembro e quem publicou todos os meses até
  * dezembro têm a mesma última evidência e regimes de transparência opostos — só a cadência
  * separa os dois.
  */
 public boolean isMonitoringIrregular(){return monitoringPublished12m!=null&&monitoringPublished12m>0&&getMonitoringRegularity()<MONITORING_MIN_REGULARITY;}
 public int getEffectiveMonitoring(LocalDate today){return monitoring>1&&(isMonitoringStale(today)||isMonitoringIrregular())?1:monitoring;}

 public int getPoints(LocalDate today){return debtor+term+guarantees+getEffectiveCoverage(today)+financials+getEffectiveMonitoring(today);}
 public BigDecimal getPercent(LocalDate today){return BigDecimal.valueOf(getPoints(today)*100L,0).divide(BigDecimal.valueOf(MAX),0,RoundingMode.HALF_UP);}

 private static Long age(LocalDate reference,LocalDate today){return reference==null?null:ChronoUnit.MONTHS.between(reference,today);}
 private static boolean stale(LocalDate reference,LocalDate today,int months){var value=age(reference,today);return value!=null&&value>=months;}

 public Long getId(){return id;} public String getSlug(){return slug;} public String getOperationName(){return operationName;} public String getCreditCode(){return creditCode;}
 public BigDecimal getAmountMillions(){return amountMillions;}
 public int getDebtor(){return debtor;} public int getTerm(){return term;} public int getGuarantees(){return guarantees;}
 public int getCoverage(){return coverage;} public int getFinancials(){return financials;} public int getMonitoring(){return monitoring;}
 public String getGapNote(){return gapNote;}
}
