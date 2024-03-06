package task.exchangerates.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cglib.core.Local;
import org.springframework.web.client.RestTemplate;
import task.exchangerates.model.entity.Rate;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class NbpApiService {
    private static final String NBP_API_URL = "http://api.nbp.pl/";


    @Cacheable
    public List<Rate> getAllRatesForToday() {
        return fetchAllRatesForToday();
    }

    public List<Rate> fetchAllRatesForDate(LocalDate date){
        String ratesA = "https://api.nbp.pl/api/exchangerates/tables/a/" + date + "/?format=json";
        String ratesB = "https://api.nbp.pl/api/exchangerates/tables/b/" + date + "/?format=json";
        String ratesC = "https://api.nbp.pl/api/exchangerates/tables/c/" + date + "/?format=json";
        String ratesJson = ratesA + ratesB + ratesC;

        List<Rate> rateList = new ArrayList<>();

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // Convert JSON array to list of Rate objects
            rateList = objectMapper.readValue(ratesJson, objectMapper.getTypeFactory().constructCollectionType(List.class, Rate.class));

        } catch (IOException e) {
            e.printStackTrace();
        }
        return rateList;
    }

    public List<Rate> fetchAllRatesForToday(){
        String ratesForTodayClassA = "http://api.nbp.pl/api/exchangerates/tables/a/today/?format=json";
        String ratesForTodayClassB = "http://api.nbp.pl/api/exchangerates/tables/b/today/?format=json";
        String ratesForTodayClassC = "http://api.nbp.pl/api/exchangerates/tables/c/today/?format=json";

        String ratesJson = ratesForTodayClassA + ratesForTodayClassB + ratesForTodayClassC;
        List<Rate> rateList = new ArrayList<>();

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // Convert JSON array to list of Rate objects
            rateList = objectMapper.readValue(ratesJson, objectMapper.getTypeFactory().constructCollectionType(List.class, Rate.class));

        } catch (IOException e) {
            e.printStackTrace();
        }
        return rateList;
    }

    @Cacheable
    public List<Rate> getRatesForDate(LocalDate date) {
        return fetchAllRatesForDate(date);
    }

//    @Cacheable
//    public List<Rate> getRatesForCurrency(String code) {
//
//        // Implement logic to fetch rates for a specific currency from NBP API
//    }

    @CacheEvict(allEntries = true)
    public void refreshCache() {
        // Implement logic to refresh cache
    }

    @CacheEvict(key = "#code")
    public void refreshCacheForCurrency(String code) {
        // Implement logic to refresh cache for a specific currency
    }
}
