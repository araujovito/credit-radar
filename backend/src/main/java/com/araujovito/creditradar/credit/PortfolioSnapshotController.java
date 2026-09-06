package com.araujovito.creditradar.credit;
import java.util.List;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/snapshots")
public class PortfolioSnapshotController { private final PortfolioSnapshotRepository repository; public PortfolioSnapshotController(PortfolioSnapshotRepository repository){this.repository=repository;} @GetMapping public List<PortfolioSnapshot> list(){return repository.findAllByOrderByReferenceDateDesc();} }
