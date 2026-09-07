package com.araujovito.creditradar.research;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Mede o que é possível saber sobre uma operação em fonte aberta — não a qualidade do crédito.
 * Cada eixo vale 2 (documento primário público), 1 (informação parcial ou de segunda mão)
 * ou 0 (ausente), de modo que o máximo por operação é 12.
 *
 * <p>O nível aqui é bruto: diz se o documento existe em fonte aberta. Para os eixos que se
 * repetem no tempo, {@link AxisCadence} guarda recência e frequência e rebaixa para parcial o
 * que envelheceu ou passou a sair fora de cadência.
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

 protected TransparencyScore(){}
 public TransparencyScore(String slug,String operationName,String creditCode,BigDecimal amountMillions,int debtor,int term,int guarantees,int coverage,int financials,int monitoring,String gapNote){
  this.slug=slug;this.operationName=operationName;this.creditCode=creditCode;this.amountMillions=amountMillions;
  this.debtor=debtor;this.term=term;this.guarantees=guarantees;this.coverage=coverage;this.financials=financials;this.monitoring=monitoring;
  this.gapNote=gapNote;}


 /** Nível bruto por eixo, antes de a cadência rebaixar o que envelheceu ou rareou. */
 public int rawLevel(String axis){return switch(axis){case "debtor"->debtor;case "term"->term;case "guarantees"->guarantees;case "coverage"->coverage;case "financials"->financials;case "monitoring"->monitoring;default->0;};}
 public static BigDecimal percentOf(int points){return BigDecimal.valueOf(points*100L,0).divide(BigDecimal.valueOf(MAX),0,RoundingMode.HALF_UP);}

 public Long getId(){return id;} public String getSlug(){return slug;} public String getOperationName(){return operationName;} public String getCreditCode(){return creditCode;}
 public BigDecimal getAmountMillions(){return amountMillions;}
 public int getDebtor(){return debtor;} public int getTerm(){return term;} public int getGuarantees(){return guarantees;}
 public int getCoverage(){return coverage;} public int getFinancials(){return financials;} public int getMonitoring(){return monitoring;}
 public String getGapNote(){return gapNote;}
}
