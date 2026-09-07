package com.araujovito.creditradar.research;
import jakarta.persistence.*;
@Entity public class DebtorResearch {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(nullable=false,unique=true) private String debtor;
 @Column(nullable=false) private String status;
 @Column(nullable=false,length=1200) private String assessment;
 @Column(nullable=false,length=1200) private String sourceUrl;
 protected DebtorResearch(){}
 public DebtorResearch(String debtor,String status,String assessment,String sourceUrl){this.debtor=debtor;this.status=status;this.assessment=assessment;this.sourceUrl=sourceUrl;}
 public Long getId(){return id;} public String getDebtor(){return debtor;} public String getStatus(){return status;} public String getAssessment(){return assessment;} public String getSourceUrl(){return sourceUrl;}
}
