package com.araujovito.creditradar.research;
import java.util.List;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/research/debtors") public class DebtorResearchController {private final DebtorResearchRepository repository;public DebtorResearchController(DebtorResearchRepository repository){this.repository=repository;}@GetMapping public List<DebtorResearch> list(){return repository.findAll();}}
