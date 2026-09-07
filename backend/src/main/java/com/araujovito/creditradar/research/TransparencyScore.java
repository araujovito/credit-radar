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
 */
@Entity
public class TransparencyScore {
 public static final int MAX = 12;
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
 /** Data da apuração de cobertura mais recente em fonte aberta. Nulo quando não há nenhuma. */
 private LocalDate coverageAsOf;
 /** Meses a partir dos quais uma apuração de cobertura deixa de valer como verificação corrente. */
 public static final int STALE_MONTHS=18;
 protected TransparencyScore(){}
 public TransparencyScore(String slug,String operationName,String creditCode,BigDecimal amountMillions,int debtor,int term,int guarantees,int coverage,int financials,int monitoring,String gapNote,LocalDate coverageAsOf){
  this.slug=slug;this.operationName=operationName;this.creditCode=creditCode;this.amountMillions=amountMillions;
  this.debtor=debtor;this.term=term;this.guarantees=guarantees;this.coverage=coverage;this.financials=financials;this.monitoring=monitoring;this.gapNote=gapNote;this.coverageAsOf=coverageAsOf;}
 public void dateCoverage(LocalDate coverageAsOf){this.coverageAsOf=coverageAsOf;}
 public LocalDate getCoverageAsOf(){return coverageAsOf;}
 public Long getCoverageAgeMonths(LocalDate today){return coverageAsOf==null?null:ChronoUnit.MONTHS.between(coverageAsOf,today);}
 /** Uma apuração vencida deixa de valer como cobertura verificada e vira evidência parcial. */
 public boolean isCoverageStale(LocalDate today){var age=getCoverageAgeMonths(today);return age!=null&&age>=STALE_MONTHS;}
 public int getEffectiveCoverage(LocalDate today){return coverage>0&&isCoverageStale(today)?1:coverage;}
 public int getPoints(LocalDate today){return debtor+term+guarantees+getEffectiveCoverage(today)+financials+monitoring;}
 public BigDecimal getPercent(LocalDate today){return BigDecimal.valueOf(getPoints(today)*100L,0).divide(BigDecimal.valueOf(MAX),0,RoundingMode.HALF_UP);}
 public Long getId(){return id;} public String getSlug(){return slug;} public String getOperationName(){return operationName;} public String getCreditCode(){return creditCode;}
 public BigDecimal getAmountMillions(){return amountMillions;}
 public int getDebtor(){return debtor;} public int getTerm(){return term;} public int getGuarantees(){return guarantees;}
 public int getCoverage(){return coverage;} public int getFinancials(){return financials;} public int getMonitoring(){return monitoring;}
 public String getGapNote(){return gapNote;}
}
