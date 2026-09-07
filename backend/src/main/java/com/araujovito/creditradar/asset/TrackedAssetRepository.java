package com.araujovito.creditradar.asset;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface TrackedAssetRepository extends JpaRepository<TrackedAsset,Long> { Optional<TrackedAsset> findByTickerIgnoreCase(String ticker); }
