package com.araujovito.creditradar.research;
import java.util.List;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/research/groups") public class EconomicGroupProfileController {private final EconomicGroupProfileRepository repository;public EconomicGroupProfileController(EconomicGroupProfileRepository repository){this.repository=repository;}@GetMapping public List<EconomicGroupProfile> list(){return repository.findAll();}}
