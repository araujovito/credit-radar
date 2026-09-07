package com.araujovito.creditradar.research;

import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/analytics/coverage") public class CoverageController {
 private final CoverageMeasurementRepository measurements;
 public CoverageController(CoverageMeasurementRepository measurements){this.measurements=measurements;}
 @GetMapping public List<CoverageMeasurement> all(){return measurements.findAll();}
}
