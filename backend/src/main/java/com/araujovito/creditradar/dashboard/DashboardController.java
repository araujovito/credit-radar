package com.araujovito.creditradar.dashboard;
import com.araujovito.creditradar.asset.*;
import com.araujovito.creditradar.credit.*;
import java.util.Map;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/dashboard") public class DashboardController {private final TrackedAssetRepository assets;private final CreditPositionRepository positions;public DashboardController(TrackedAssetRepository assets,CreditPositionRepository positions){this.assets=assets;this.positions=positions;}@GetMapping public Map<String,Object> summary(){return Map.of("assets",assets.count(),"positions",positions.count(),"byType",assets.findAll().stream().collect(java.util.stream.Collectors.groupingBy(a->a.getType().name(),java.util.stream.Collectors.counting())));}}
