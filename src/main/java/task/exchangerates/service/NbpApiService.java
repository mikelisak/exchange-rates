package task.exchangerates.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import task.exchangerates.model.entity.Rate;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class NbpApiService implements NbpServiceApiClient {
    private static final String NBP_API_URL = "https://api.nbp.pl/api/exchangerates/";

    public List<Rate> getRatesByDate(LocalDate date) throws IOException {
        if(date != null){
            return getListOfRatesByDate(date);
        }
        return getListOfRatesByDate(LocalDate.now());
    }

    public Rate getRatesByCurrency(String code) throws IOException {
       return getRateByCode(code);
    }
    @Cacheable(cacheNames = "rates", key = "#date")
    public List<Rate> getListOfRatesByDate(LocalDate date) throws IOException {
        System.out.println("cache");
        String stringUrl = NBP_API_URL + "tables/a/" + date.toString() + "?format=json";
        URL ratesJson = new URL(stringUrl);

        URLConnection request = ratesJson.openConnection();
        request.connect();
        // Convert to a JSON object to print data
        JsonParser jp = new JsonParser(); //from gson
        JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent())); //Convert the input stream to a json element
        JsonArray jsonObject = root.getAsJsonArray();

        JsonArray ratesArray = jsonObject.get(0).getAsJsonObject().get("rates").getAsJsonArray();
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
    }

    @Cacheable(cacheNames = "rates", key = "#code")
    public Rate getRateByCode(String code) throws IOException {
        System.out.println("cache");
        String getRatesForCurrency = NBP_API_URL + "/rates/a/" + code + "?format=json";
        URL url = new URL(getRatesForCurrency);
        URLConnection request = url.openConnection();
        request.connect();
        // Convert to a JSON object to print data
        JsonParser jp = new JsonParser(); //from gson
        JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent())); //Convert the input stream to a json element
        JsonObject jsonObject = root.getAsJsonObject();

        JsonArray jArray = jsonObject.getAsJsonArray("rates");
        BigDecimal mid = null;
        LocalDate effectiveDate = null;
        for (int i = 0; i < jArray.size(); i++) {
            JsonObject object = jArray.get(i).getAsJsonObject();

            mid = object.get("mid").getAsBigDecimal();
            effectiveDate = LocalDate.parse(object.get("effectiveDate").getAsString());
        }
        return Rate.builder()
                .currency(jsonObject.get("currency").toString())
                .code(jsonObject.get("code").toString())
                .mid(mid)
                .date(effectiveDate)
                .build();
    }
    @Scheduled(cron = "0 6 * * * *")
    @CacheEvict(allEntries = true)
    public void refreshCache() {}

    @CacheEvict(cacheNames = "rates", key = "#code")
    public void refreshCacheForCurrency(String code) throws IOException {
        System.out.println("refresh");
        getRateByCode(code);
    }
}
