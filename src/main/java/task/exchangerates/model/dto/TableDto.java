package task.exchangerates.model.dto;

import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

@Data
@ToString
public class TableDto {
    private String table;
    private String no;
    private LocalDate effectiveDate;
    private List<TableRateInfoDto> rates;

}
