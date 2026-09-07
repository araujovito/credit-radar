package com.araujovito.creditradar.research;
import java.util.List;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/research/cri-documents") public class CriDocumentController {
 private final CriDocumentRepository repository;
 public CriDocumentController(CriDocumentRepository repository){this.repository=repository;}
 @GetMapping public List<CriDocument> list(){return repository.findAll();}
}
