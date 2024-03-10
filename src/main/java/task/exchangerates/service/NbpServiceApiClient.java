package task.exchangerates.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import task.exchangerates.model.dto.TableDto;
import task.exchangerates.model.dto.RateDto;

import java.io.IOException;

@FeignClient(value = "exchange-rates", url = "https://api.nbp.pl/api/exchangerates")
public interface NbpServiceApiClient {
    @RequestMapping(method = RequestMethod.GET, value = "/rates/a/{code}?format=json")
    RateDto getRateByCurrency(@PathVariable("code") String code) throws IOException;
    @RequestMapping(method = RequestMethod.GET, value = "/tables/a/{date}?format=json")
    TableDto[] getRatesByDate(@PathVariable("date") String date) throws IOException;
}
