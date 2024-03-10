package task.exchangerates.model.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class RateInfoDto {
    private String no;
    private LocalDate effectiveDate;
    private BigDecimal mid;
}
