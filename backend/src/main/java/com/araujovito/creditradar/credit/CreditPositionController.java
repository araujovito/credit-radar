package com.araujovito.creditradar.credit;
import java.util.List;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/positions")
public class CreditPositionController { private final CreditPositionRepository repository; public CreditPositionController(CreditPositionRepository repository){this.repository=repository;} @GetMapping public List<CreditPosition> list(){return repository.findAllByOrderByPortfolioWeightDesc();} }
