package com.araujovito.creditradar.research;

import jakarta.persistence.*;

@Entity
public class DebtorDossier {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(nullable=false,unique=true) private String slug;
 @Column(nullable=false) private String operationName;
 @Column(nullable=false) private String legalName;
 @Column(nullable=false) private String cnpj;
 @Column(nullable=false) private String economicGroup;
 @Column(nullable=false) private String sector;
 @Column(nullable=false) private String entityType;
 @Column(nullable=false,length=1400) private String debtAndLiquidity;
 @Column(nullable=false,length=1400) private String cashGeneration;
 @Column(nullable=false,length=1400) private String guarantees;
 @Column(nullable=false,length=1400) private String alertHistory;
 @Column(nullable=false,length=1400) private String sourceNote;
 @Column(nullable=false,length=1200) private String sourceUrl;
 protected DebtorDossier(){}
 public DebtorDossier(String slug,String operationName,String legalName,String cnpj,String economicGroup,String sector,String entityType,String debtAndLiquidity,String cashGeneration,String guarantees,String alertHistory,String sourceNote,String sourceUrl){this.slug=slug;this.operationName=operationName;this.legalName=legalName;this.cnpj=cnpj;this.economicGroup=economicGroup;this.sector=sector;this.entityType=entityType;this.debtAndLiquidity=debtAndLiquidity;this.cashGeneration=cashGeneration;this.guarantees=guarantees;this.alertHistory=alertHistory;this.sourceNote=sourceNote;this.sourceUrl=sourceUrl;}
 public Long getId(){return id;} public String getSlug(){return slug;} public String getOperationName(){return operationName;} public String getLegalName(){return legalName;} public String getCnpj(){return cnpj;} public String getEconomicGroup(){return economicGroup;} public String getSector(){return sector;} public String getEntityType(){return entityType;} public String getDebtAndLiquidity(){return debtAndLiquidity;} public String getCashGeneration(){return cashGeneration;} public String getGuarantees(){return guarantees;} public String getAlertHistory(){return alertHistory;} public String getSourceNote(){return sourceNote;} public String getSourceUrl(){return sourceUrl;}
}
