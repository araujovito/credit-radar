package com.araujovito.creditradar.credit;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
public record ImportDocumentRequest(@NotBlank String ticker,@NotBlank String title,@NotNull LocalDate referenceDate,@NotBlank @Pattern(regexp="https?://.+") String sourceUrl,@DecimalMin("0.00") @DecimalMax("100.00") BigDecimal targetAssetsPercent,@DecimalMin("0.00") @DecimalMax("100.00") BigDecimal lciPercent,@DecimalMin("0.00") @DecimalMax("100.00") BigDecimal cashPercent) {}
