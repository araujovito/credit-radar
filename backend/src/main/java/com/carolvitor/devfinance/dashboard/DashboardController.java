package com.carolvitor.devfinance.dashboard;
import com.carolvitor.devfinance.expense.*;
import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.*;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/dashboard")
public class DashboardController {
  private final TransactionRepository repository;
  public DashboardController(TransactionRepository repository) { this.repository = repository; }
  @GetMapping public Summary summary(@RequestParam(required=false) YearMonth month) {
    YearMonth selected=month==null?YearMonth.now():month;
    var items=repository.findByOccurredOnBetweenOrderByOccurredOnDesc(selected.atDay(1),selected.atEndOfMonth());
    BigDecimal income=items.stream().filter(t->t.getType()==TransactionType.INCOME).map(Transaction::getAmount).reduce(BigDecimal.ZERO,BigDecimal::add);
    BigDecimal expense=items.stream().filter(t->t.getType()==TransactionType.EXPENSE).map(Transaction::getAmount).reduce(BigDecimal.ZERO,BigDecimal::add);
    var byCategory=new LinkedHashMap<String, BigDecimal>();
    items.stream().filter(t->t.getType()==TransactionType.EXPENSE).forEach(t -> byCategory.merge(t.getCategory().getName(),t.getAmount(),BigDecimal::add));
    return new Summary(income, expense, income.subtract(expense), byCategory);
  }
  public record Summary(BigDecimal income, BigDecimal expense, BigDecimal balance, Map<String, BigDecimal> expensesByCategory) {}
}
