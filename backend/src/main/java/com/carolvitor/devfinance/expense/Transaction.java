package com.carolvitor.devfinance.expense;

import com.carolvitor.devfinance.category.Category;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity @Table(name = "transactions")
public class Transaction {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
  @Column(nullable = false) private String description;
  @Column(nullable = false, precision = 12, scale = 2) private BigDecimal amount;
  @Enumerated(EnumType.STRING) @Column(nullable = false) private TransactionType type;
  @Column(nullable = false) private LocalDate occurredOn;
  @ManyToOne(optional = false) private Category category;
  protected Transaction() {}
  public Transaction(String description, BigDecimal amount, TransactionType type, LocalDate occurredOn, Category category) { this.description=description; this.amount=amount; this.type=type; this.occurredOn=occurredOn; this.category=category; }
  public Long getId(){return id;} public String getDescription(){return description;} public BigDecimal getAmount(){return amount;} public TransactionType getType(){return type;} public LocalDate getOccurredOn(){return occurredOn;} public Category getCategory(){return category;}
}
