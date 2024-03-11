package task.exchangerates.service;

import org.springframework.stereotype.Component;
import task.exchangerates.model.dto.RateDto;
import task.exchangerates.model.dto.RateInfoDto;
import task.exchangerates.model.dto.TableDto;
import task.exchangerates.model.dto.TableRateInfoDto;
import task.exchangerates.model.entity.Rate;

import java.util.ArrayList;
import java.util.List;
/**
 *
 * Class which converts DTO's to Rate Entity
 *
 */
@Component
public class JsonToRateConverter {

    public Rate convertRateDtoToRate(RateDto rateDto){
        RateInfoDto infoDto = rateDto.getRates()
                .stream()
                .findFirst()
                .orElse(RateInfoDto.builder().build());

        return Rate.builder()
                .currency(rateDto.getCurrency())
                .code(rateDto.getCode())
                .date(infoDto.getEffectiveDate())
                .mid(infoDto.getMid())
                .build();
    }

    public List<Rate> convertRateListToRate(TableDto tableDto){
        List<Rate> rates = new ArrayList<>();
        tableDto.getRates().forEach(e -> rates.add(convertRateInfoDtoToRate(e, tableDto)));
        return rates;
    }

    private Rate convertRateInfoDtoToRate(TableRateInfoDto rateInfoDto, TableDto tableDto){
        return Rate.builder()
                .currency(rateInfoDto.getCurrency())
                .code(rateInfoDto.getCode())
                .mid(rateInfoDto.getMid())
                .date(tableDto.getEffectiveDate())
                .build();

    }
}
