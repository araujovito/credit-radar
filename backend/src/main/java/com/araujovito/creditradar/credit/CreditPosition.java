package com.araujovito.creditradar.credit;
import com.araujovito.creditradar.asset.TrackedAsset;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class CreditPosition {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
  @ManyToOne(optional=false) private TrackedAsset asset;
  @ManyToOne(optional=false) private SourceDocument sourceDocument;
  @Enumerated(EnumType.STRING) @Column(nullable=false) private InstrumentType instrumentType;
  @Column(nullable=false) private String issuer;
  private String sector; private String indexer; private String rating; private LocalDate maturity;
  @Column(precision=6,scale=3) private BigDecimal portfolioWeight;
  @Column(precision=12,scale=2) private BigDecimal amountMillions;
  private String guarantees;
  @Column(nullable=false) private LocalDate referenceDate;
  protected CreditPosition() {}
  public CreditPosition(TrackedAsset asset,SourceDocument sourceDocument,InstrumentType instrumentType,String issuer,String sector,String indexer,String rating,LocalDate maturity,BigDecimal portfolioWeight,BigDecimal amountMillions,String guarantees,LocalDate referenceDate){this.asset=asset;this.sourceDocument=sourceDocument;this.instrumentType=instrumentType;this.issuer=issuer;this.sector=sector;this.indexer=indexer;this.rating=rating;this.maturity=maturity;this.portfolioWeight=portfolioWeight;this.amountMillions=amountMillions;this.guarantees=guarantees;this.referenceDate=referenceDate;}
  public Long getId(){return id;} public TrackedAsset getAsset(){return asset;} public SourceDocument getSourceDocument(){return sourceDocument;} public InstrumentType getInstrumentType(){return instrumentType;} public String getIssuer(){return issuer;} public String getSector(){return sector;} public String getIndexer(){return indexer;} public String getRating(){return rating;} public LocalDate getMaturity(){return maturity;} public BigDecimal getPortfolioWeight(){return portfolioWeight;} public BigDecimal getAmountMillions(){return amountMillions;} public String getGuarantees(){return guarantees;} public LocalDate getReferenceDate(){return referenceDate;}
}
