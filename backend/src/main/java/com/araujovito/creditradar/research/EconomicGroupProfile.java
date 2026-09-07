package com.araujovito.creditradar.research;
import jakarta.persistence.*;

@Entity
public class EconomicGroupProfile {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(nullable=false,unique=true) private String name;
 @Column(nullable=false) private String sector;
 @Column(nullable=false) private String publicStatus;
 private String ticker;
 @Column(nullable=false,length=1200) private String sourceUrl;
 @Column(nullable=false,length=1200) private String relationshipNote;
 @Column(nullable=false) private String confidence;
 protected EconomicGroupProfile(){}
 public EconomicGroupProfile(String name,String sector,String publicStatus,String ticker,String sourceUrl,String relationshipNote,String confidence){this.name=name;this.sector=sector;this.publicStatus=publicStatus;this.ticker=ticker;this.sourceUrl=sourceUrl;this.relationshipNote=relationshipNote;this.confidence=confidence;}
 public Long getId(){return id;} public String getName(){return name;} public String getSector(){return sector;} public String getPublicStatus(){return publicStatus;} public String getTicker(){return ticker;} public String getSourceUrl(){return sourceUrl;} public String getRelationshipNote(){return relationshipNote;} public String getConfidence(){return confidence;}
}
