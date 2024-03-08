package task.exchangerates.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import task.exchangerates.model.entity.Rate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class NbpApiService {
    private static final String NBP_API_URL = "https://api.nbp.pl/api/exchangerates/";
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final Logger logger = LoggerFactory.getLogger(NbpApiService.class);

    public List<Rate> getAllRates(LocalDate date) throws IOException {
        if(date != null){
            return getRatesForDate(date);
        }
        return getRatesForToday();
    }

    public List<Rate> getRatesForToday() throws IOException {
        return getListOfRatesByDate(LocalDate.now());
    }

    public List<Rate> getRatesForDate(LocalDate date) throws IOException {
        return getListOfRatesByDate(date);
    }

    public Rate getRatesForCurrency(String code) throws MalformedURLException {
       return getRateByCode(code);
    }
    @Cacheable(cacheNames = "rates", key = "date")
    public List<Rate> getListOfRatesByDate(LocalDate date) throws IOException {
        System.out.println("cache");
        String stringUrl = NBP_API_URL + "/tables/a/" + date.toString() + "?format=json";
        URL ratesJson = new URL(stringUrl);
        try (InputStream input = ratesJson.openStream()) {
            InputStreamReader isr = new InputStreamReader(input);
            BufferedReader reader = new BufferedReader(isr);
            StringBuilder json = new StringBuilder();
            int c;
            while ((c = reader.read()) != -1) {
                json.append((char) c);
            }
            JsonArray convertedObject = new Gson().fromJson(String.valueOf(json), JsonArray.class);
            JsonArray ratesArray = convertedObject.get(0).getAsJsonObject().get("rates").getAsJsonArray();
            List<Rate> rateList = new ArrayList<>();
            for (int i = 0; i < ratesArray.size(); i++) {
                JsonObject object = ratesArray.get(i).getAsJsonObject();
                rateList.add(Rate.builder()
                        .currency(String.valueOf(object.get("currency")))
                        .code(String.valueOf(object.get("code")))
                        .mid((object.get("mid").getAsBigDecimal()))
                        .build());
            }
            return rateList;
        }catch (IOException e) {
            logger.error(String.valueOf(e));
        }
        return new ArrayList<>();
    }
    @Cacheable(cacheNames = "rates", key = "code")
    public Rate getRateByCode(String code) throws MalformedURLException {
        String getRatesForCurrency = NBP_API_URL + "/a/" + code + "?format=json";
        URL url = new URL(getRatesForCurrency);
        try (InputStream input = url.openStream()) {
            InputStreamReader isr = new InputStreamReader(input);
            BufferedReader reader = new BufferedReader(isr);
            StringBuilder json = new StringBuilder();
            int c;
            while ((c = reader.read()) != -1) {
                json.append((char) c);
            }
            JsonObject convertedObject = new Gson().fromJson(String.valueOf(json), JsonObject.class);

            JsonArray jArray =  convertedObject.getAsJsonArray("rates");
            BigDecimal mid = null;
            LocalDate effectiveDate = null;
            for (int i = 0; i < jArray.size(); i++) {
                JsonObject object = jArray.get(i).getAsJsonObject();

                mid = object.get("mid").getAsBigDecimal();
                effectiveDate = LocalDate.parse(object.get("effectiveDate").getAsString());
            }
            return Rate.builder()
                    .currency(convertedObject.get("currency").toString())
                    .code(convertedObject.get("code").toString())
                    .mid(mid)
                    .date(effectiveDate)
                    .build();
        }catch (IOException e) {
            logger.error(String.valueOf(e));
        }
        return Rate.builder().build();
    }
    @Scheduled(cron = "0 6 * * * *")
    @CacheEvict(allEntries = true)
    public void refreshCache() {}

    @CacheEvict(cacheNames = "rates", key = "#code")
    public void refreshCacheForCurrency(String code) throws MalformedURLException {
        getRateByCode(code);
    }
}
