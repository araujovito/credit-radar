package com.araujovito.creditradar.asset;
import jakarta.persistence.*;

@Entity
public class TrackedAsset {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
  @Column(nullable=false, unique=true) private String ticker;
  @Column(nullable=false) private String name;
  @Enumerated(EnumType.STRING) @Column(nullable=false) private AssetType type;
  @Column(nullable=false) private String manager;
  protected TrackedAsset() {}
  public TrackedAsset(String ticker,String name,AssetType type,String manager){this.ticker=ticker;this.name=name;this.type=type;this.manager=manager;}
  public Long getId(){return id;} public String getTicker(){return ticker;} public String getName(){return name;} public AssetType getType(){return type;} public String getManager(){return manager;}
}
