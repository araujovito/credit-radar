package com.araujovito.creditradar.credit;
import com.araujovito.creditradar.asset.TrackedAsset;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class PortfolioSnapshot {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
  @ManyToOne(optional=false) private TrackedAsset asset;
  @ManyToOne(optional=false) private SourceDocument sourceDocument;
  @Column(nullable=false) private LocalDate referenceDate;
  @Column(precision=6,scale=2) private BigDecimal targetAssetsPercent;
  @Column(precision=6,scale=2) private BigDecimal lciPercent;
  @Column(precision=6,scale=2) private BigDecimal cashPercent;
  protected PortfolioSnapshot() {}
  public PortfolioSnapshot(TrackedAsset asset,SourceDocument sourceDocument,LocalDate referenceDate,BigDecimal targetAssetsPercent,BigDecimal lciPercent,BigDecimal cashPercent){this.asset=asset;this.sourceDocument=sourceDocument;this.referenceDate=referenceDate;this.targetAssetsPercent=targetAssetsPercent;this.lciPercent=lciPercent;this.cashPercent=cashPercent;}
  public Long getId(){return id;} public TrackedAsset getAsset(){return asset;} public SourceDocument getSourceDocument(){return sourceDocument;} public LocalDate getReferenceDate(){return referenceDate;} public BigDecimal getTargetAssetsPercent(){return targetAssetsPercent;} public BigDecimal getLciPercent(){return lciPercent;} public BigDecimal getCashPercent(){return cashPercent;}
}
