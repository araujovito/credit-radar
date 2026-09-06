package com.araujovito.creditradar.credit;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
public interface PortfolioSnapshotRepository extends JpaRepository<PortfolioSnapshot,Long> { List<PortfolioSnapshot> findAllByOrderByReferenceDateDesc(); }
