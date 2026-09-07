package com.araujovito.creditradar.research;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AxisCadenceRepository extends JpaRepository<AxisCadence,Long>{List<AxisCadence> findBySlug(String slug);}
