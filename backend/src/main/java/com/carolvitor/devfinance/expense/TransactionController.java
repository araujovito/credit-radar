package com.carolvitor.devfinance.expense;

import com.carolvitor.devfinance.category.CategoryRepository;
import jakarta.validation.Valid;
import java.time.*;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/transactions")
public class TransactionController {
  private final TransactionRepository transactions; private final CategoryRepository categories;
  public TransactionController(TransactionRepository transactions, CategoryRepository categories) { this.transactions=transactions; this.categories=categories; }
  @GetMapping public List<Transaction> list(@RequestParam(required=false) YearMonth month) { YearMonth selected=month==null?YearMonth.now():month; return transactions.findByOccurredOnBetweenOrderByOccurredOnDesc(selected.atDay(1),selected.atEndOfMonth()); }
  @PostMapping @ResponseStatus(HttpStatus.CREATED) public Transaction create(@Valid @RequestBody CreateTransactionRequest request) {
    var category=categories.findById(request.categoryId()).orElseThrow(() -> new IllegalArgumentException("Categoria não encontrada"));
    return transactions.save(new Transaction(request.description(),request.amount(),request.type(),request.occurredOn(),category));
  }
  @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void delete(@PathVariable Long id) { transactions.deleteById(id); }
}
