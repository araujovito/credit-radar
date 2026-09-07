package com.araujovito.creditradar.credit;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
public interface CreditPositionRepository extends JpaRepository<CreditPosition,Long> { List<CreditPosition> findAllByOrderByPortfolioWeightDesc(); List<CreditPosition> findByIssuerContainingIgnoreCaseOrderByReferenceDateDesc(String issuer); List<CreditPosition> findByAssetTickerIgnoreCase(String ticker); List<CreditPosition> findByAssetTickerIgnoreCaseAndReferenceDate(String ticker, java.time.LocalDate referenceDate); }
