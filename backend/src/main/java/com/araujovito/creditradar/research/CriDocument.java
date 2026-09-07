package com.araujovito.creditradar.research;

import jakarta.persistence.*;

@Entity
public class CriDocument {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(nullable=false,unique=true) private String creditCode;
 @Column(nullable=false) private String operationName;
 @Column(nullable=false) private String issuer;
 @Column(nullable=false) private String sourceType;
 @Column(nullable=false) private String availability;
 @Column(nullable=false,length=1200) private String finding;
 @Column(nullable=false,length=1200) private String sourceUrl;
 protected CriDocument(){}
 public CriDocument(String creditCode,String operationName,String issuer,String sourceType,String availability,String finding,String sourceUrl){this.creditCode=creditCode;this.operationName=operationName;this.issuer=issuer;this.sourceType=sourceType;this.availability=availability;this.finding=finding;this.sourceUrl=sourceUrl;}
 public Long getId(){return id;} public String getCreditCode(){return creditCode;} public String getOperationName(){return operationName;} public String getIssuer(){return issuer;} public String getSourceType(){return sourceType;} public String getAvailability(){return availability;} public String getFinding(){return finding;} public String getSourceUrl(){return sourceUrl;}
}
