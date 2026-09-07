package com.araujovito.creditradar.research;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Cobertura calculada pelo Credit Radar a partir de dados públicos, quando o agente fiduciário
 * não publica LTV nem índice de cobertura. Não conta para o score de transparência: o score mede
 * o que o mercado divulga, e cálculo próprio não torna a operação mais transparente.
 */
@Entity
public class CoverageMeasurement {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(nullable=false,unique=true) private String slug;
 @Column(nullable=false) private LocalDate asOfDate;
 @Column(nullable=false) private String scope;
 @Column(nullable=false,precision=18,scale=2) private BigDecimal collateralValue;
 @Column(nullable=false,precision=18,scale=2) private BigDecimal securedBalance;
 @Column(nullable=false,precision=6,scale=1) private BigDecimal coveragePercent;
 @Column(nullable=false,length=1600) private String method;
 @Column(nullable=false,length=1600) private String caveat;
 @Column(nullable=false,length=800) private String sourceNote;
 protected CoverageMeasurement(){}
 public CoverageMeasurement(String slug,LocalDate asOfDate,String scope,BigDecimal collateralValue,BigDecimal securedBalance,BigDecimal coveragePercent,String method,String caveat,String sourceNote){
  this.slug=slug;this.asOfDate=asOfDate;this.scope=scope;this.collateralValue=collateralValue;this.securedBalance=securedBalance;
  this.coveragePercent=coveragePercent;this.method=method;this.caveat=caveat;this.sourceNote=sourceNote;}
 public Long getId(){return id;} public String getSlug(){return slug;} public LocalDate getAsOfDate(){return asOfDate;}
 public String getScope(){return scope;} public BigDecimal getCollateralValue(){return collateralValue;}
 public BigDecimal getSecuredBalance(){return securedBalance;} public BigDecimal getCoveragePercent(){return coveragePercent;}
 public String getMethod(){return method;} public String getCaveat(){return caveat;} public String getSourceNote(){return sourceNote;}
}
