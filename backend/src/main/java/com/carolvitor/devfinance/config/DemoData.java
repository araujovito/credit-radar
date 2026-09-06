package com.carolvitor.devfinance.config;
import com.carolvitor.devfinance.category.*;
import com.carolvitor.devfinance.expense.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.*;

@Configuration
public class DemoData {
  @Bean CommandLineRunner load(CategoryRepository categories, TransactionRepository transactions) { return args -> {
    if (categories.count() > 0) return;
    var work=categories.save(new Category("Trabalho", "#22c55e")); var food=categories.save(new Category("Alimentação", "#f97316")); var home=categories.save(new Category("Moradia", "#6366f1"));
    var today=LocalDate.now();
    transactions.save(new Transaction("Salário",new BigDecimal("6800.00"),TransactionType.INCOME,today.withDayOfMonth(5),work));
    transactions.save(new Transaction("Aluguel",new BigDecimal("1800.00"),TransactionType.EXPENSE,today.withDayOfMonth(6),home));
    transactions.save(new Transaction("Mercado",new BigDecimal("540.80"),TransactionType.EXPENSE,today.withDayOfMonth(8),food));
  }; }
}
