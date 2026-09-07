package com.araujovito.creditradar.research;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransparencyScoreRepository extends JpaRepository<TransparencyScore,Long>{Optional<TransparencyScore> findBySlug(String slug);}
