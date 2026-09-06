package com.araujovito.creditradar.credit;
import java.util.List;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/documents")
public class SourceDocumentController { private final SourceDocumentRepository repository; public SourceDocumentController(SourceDocumentRepository repository){this.repository=repository;} @GetMapping public List<SourceDocument> list(){return repository.findAllByOrderByReferenceDateDesc();} }
