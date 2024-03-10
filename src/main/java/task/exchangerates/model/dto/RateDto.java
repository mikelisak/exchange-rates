package task.exchangerates.model.dto;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@ToString
@Data
public class RateDto {
    private String table;
    private String currency;
    private String code;
    private List<RateInfoDto> rates;
}
