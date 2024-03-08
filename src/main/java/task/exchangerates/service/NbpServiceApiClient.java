package task.exchangerates.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import task.exchangerates.model.entity.Rate;

import java.io.IOException;
import java.net.MalformedURLException;
import java.time.LocalDate;
import java.util.List;

@FeignClient(value = "exchangerates", url = "https://api.nbp.pl/api/exchangerates/rates")
public interface NbpServiceApiClient {
    @RequestMapping(method = RequestMethod.GET, value = "/a/{code}?format=json")
    Rate getRatesByCurrency(@PathVariable("code")String code) throws IOException;
    @RequestMapping(method = RequestMethod.GET, value = "/tables/a/{date}?format=json")
    List<Rate> getRatesByDate(@PathVariable("date") LocalDate date) throws IOException;

    @RequestMapping(method = RequestMethod.GET, value = "/refresh-cache/{code}")
    void refreshCacheForCurrency(@PathVariable("code") String code) throws IOException;

}
