package task.exchangerates.model.entity;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Rate {
    private String currency;
    private String code;
    private BigDecimal mid;
    private BigDecimal bid;
    private BigDecimal ask;
    private LocalDate date;

}
