package com.carolvitor.devfinance.expense;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
public interface TransactionRepository extends JpaRepository<Transaction, Long> { List<Transaction> findByOccurredOnBetweenOrderByOccurredOnDesc(LocalDate start, LocalDate end); }
