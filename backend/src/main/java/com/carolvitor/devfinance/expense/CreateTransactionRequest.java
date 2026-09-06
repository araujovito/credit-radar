package com.carolvitor.devfinance.expense;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
public record CreateTransactionRequest(@NotBlank String description, @NotNull @Positive BigDecimal amount, @NotNull TransactionType type, @NotNull LocalDate occurredOn, @NotNull Long categoryId) {}
