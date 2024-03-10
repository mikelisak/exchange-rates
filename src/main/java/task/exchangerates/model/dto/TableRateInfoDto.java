package task.exchangerates.model.dto;

import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@ToString
public class TableRateInfoDto {
    private String currency;
    private String code;
    private BigDecimal mid;

}
