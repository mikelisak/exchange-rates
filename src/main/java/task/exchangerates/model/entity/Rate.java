package task.exchangerates.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Rate {
    private String currency;
    private String code;
    private BigDecimal mid;
    private BigDecimal bid;
    private BigDecimal ask;
    private LocalDate date;
}
