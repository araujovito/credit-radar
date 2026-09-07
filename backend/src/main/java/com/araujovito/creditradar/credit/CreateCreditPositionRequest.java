package com.araujovito.creditradar.credit;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
public record CreateCreditPositionRequest(@NotBlank String ticker,@NotNull Long sourceDocumentId,@NotNull InstrumentType instrumentType,@NotBlank String issuer,@NotBlank String sector,@NotBlank String indexer,String rating,LocalDate maturity,@DecimalMin("0.00") @DecimalMax("100.00") BigDecimal portfolioWeight,@PositiveOrZero BigDecimal amountMillions,@NotBlank String guarantees,@NotNull LocalDate referenceDate) {}
