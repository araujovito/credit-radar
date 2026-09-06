package com.araujovito.creditradar.credit;
import com.araujovito.creditradar.asset.TrackedAsset;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class SourceDocument {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
  @ManyToOne(optional=false) private TrackedAsset asset;
  @Column(nullable=false) private String title;
  @Column(nullable=false) private LocalDate referenceDate;
  @Column(nullable=false, length=1200) private String sourceUrl;
  protected SourceDocument() {}
  public SourceDocument(TrackedAsset asset,String title,LocalDate referenceDate,String sourceUrl){this.asset=asset;this.title=title;this.referenceDate=referenceDate;this.sourceUrl=sourceUrl;}
  public Long getId(){return id;} public TrackedAsset getAsset(){return asset;} public String getTitle(){return title;} public LocalDate getReferenceDate(){return referenceDate;} public String getSourceUrl(){return sourceUrl;}
}
