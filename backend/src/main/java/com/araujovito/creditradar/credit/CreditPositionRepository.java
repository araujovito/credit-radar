package com.araujovito.creditradar.credit;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
public interface CreditPositionRepository extends JpaRepository<CreditPosition,Long> { List<CreditPosition> findAllByOrderByPortfolioWeightDesc(); }
