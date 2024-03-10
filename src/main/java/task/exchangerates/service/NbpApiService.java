package task.exchangerates.service;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import task.exchangerates.model.dto.RateDto;
import task.exchangerates.model.dto.TableDto;
import task.exchangerates.model.entity.Rate;
import task.exchangerates.rabbitmq.RabbitMQProducer;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.String.format;

@RequiredArgsConstructor
@Service
@Component
@CacheConfig(cacheNames={"rate"})
public class NbpApiService {
    private static final Logger LOGGER = LoggerFactory.getLogger(NbpApiService.class);
    private final NbpServiceApiClient nbpServiceApiClient;
    private final JsonToRateConverter jsonToRateConverter;
    private final RabbitMQProducer rabbitMQProducer;

    public List<Rate> getRatesByDate(LocalDate date) throws IOException {
        if(date != null){
            return getListOfRatesByDate(date);
        }
        return getListOfRatesByDate(LocalDate.now());
    }

    public Rate getRateByCurrency(String code) throws IOException {
        return getRateByCode(code);
    }

    @Cacheable(key = "#date")
    public List<Rate> getListOfRatesByDate(LocalDate date) throws IOException {
        try {
            TableDto tableOfRates = Arrays.stream(nbpServiceApiClient.getRatesByDate(date.toString()))
                    .toList()
                    .stream()
                    .findFirst()
                    .orElse(TableDto.builder()
                            .build());
            return jsonToRateConverter.convertRateListToRate(tableOfRates);
        } catch (FeignException e) {
            LOGGER.error(e.getMessage());
        }
        return new ArrayList<>();
    }

    @Cacheable(key = "#code")
    public Rate getRateByCode(String code) throws IOException {
        try {
            RateDto rate = nbpServiceApiClient.getRateByCurrency(code);
            return jsonToRateConverter.convertRateDtoToRate(rate);
        } catch (FeignException e) {
            LOGGER.error(e.getMessage());
        }
        return Rate.builder().build();
    }

    //Cache will be cleaned every weekday at 6 am
    @Scheduled(cron = "0 0 6 * * MON-FRI")
    @CacheEvict(allEntries = true)
    public void evictAllCacheValues() {
        LOGGER.info("Cleaning cache");
    }

    @CacheEvict(key = "#code")
    public void refreshCacheForCurrency(String code) throws IOException {
        rabbitMQProducer.sendMessage(format("Refreshing cache for rate with code: %s", code));
        getRateByCode(code);
    }
}
