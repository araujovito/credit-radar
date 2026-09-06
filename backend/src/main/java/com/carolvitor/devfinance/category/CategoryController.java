package com.carolvitor.devfinance.category;

import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/categories")
public class CategoryController {
  private final CategoryRepository repository;
  public CategoryController(CategoryRepository repository) { this.repository = repository; }
  @GetMapping public List<Category> all() { return repository.findAll(); }
}
