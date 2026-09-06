package com.araujovito.creditradar.credit;
import com.araujovito.creditradar.asset.TrackedAsset;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class CreditPosition {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
  @ManyToOne(optional=false) private TrackedAsset asset;
  @Enumerated(EnumType.STRING) @Column(nullable=false) private InstrumentType instrumentType;
  @Column(nullable=false) private String issuer;
  private String sector; private String indexer; private String rating; private LocalDate maturity;
  @Column(precision=6,scale=3) private BigDecimal portfolioWeight;
  @Column(nullable=false) private LocalDate referenceDate;
  protected CreditPosition() {}
  public CreditPosition(TrackedAsset asset,InstrumentType instrumentType,String issuer,String sector,String indexer,String rating,LocalDate maturity,BigDecimal portfolioWeight,LocalDate referenceDate){this.asset=asset;this.instrumentType=instrumentType;this.issuer=issuer;this.sector=sector;this.indexer=indexer;this.rating=rating;this.maturity=maturity;this.portfolioWeight=portfolioWeight;this.referenceDate=referenceDate;}
  public Long getId(){return id;} public TrackedAsset getAsset(){return asset;} public InstrumentType getInstrumentType(){return instrumentType;} public String getIssuer(){return issuer;} public String getSector(){return sector;} public String getIndexer(){return indexer;} public String getRating(){return rating;} public LocalDate getMaturity(){return maturity;} public BigDecimal getPortfolioWeight(){return portfolioWeight;} public LocalDate getReferenceDate(){return referenceDate;}
}
