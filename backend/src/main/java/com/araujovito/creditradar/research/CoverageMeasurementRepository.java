package com.araujovito.creditradar.research;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoverageMeasurementRepository extends JpaRepository<CoverageMeasurement,Long>{Optional<CoverageMeasurement> findBySlug(String slug);}
