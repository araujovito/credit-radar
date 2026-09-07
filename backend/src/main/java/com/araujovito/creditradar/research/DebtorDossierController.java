package com.araujovito.creditradar.research;
import java.util.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/debtors") public class DebtorDossierController {
 private final DebtorDossierRepository repository; public DebtorDossierController(DebtorDossierRepository repository){this.repository=repository;}
 @GetMapping public List<DebtorDossier> list(){return repository.findAll();}
 @GetMapping("/{slug}") public DebtorDossier one(@PathVariable String slug){return repository.findBySlug(slug).orElseThrow(()->new NoSuchElementException("Dossiê não encontrado"));}
 @ExceptionHandler(NoSuchElementException.class) @ResponseStatus(HttpStatus.NOT_FOUND) void notFound(){}
}
