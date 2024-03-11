package task.exchangerates.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import task.exchangerates.model.dto.RateDto;
import task.exchangerates.model.dto.TableDto;

import java.io.IOException;

/**
 *
 * Feign client which fetches data from {@link "https://api.nbp.pl/"}
 * and returns specific DTO's
 *
 */
@FeignClient(value = "exchange-rates", url = "https://api.nbp.pl/api/exchangerates")
public interface NbpServiceApiClient {
    @RequestMapping(method = RequestMethod.GET, value = "/rates/a/{code}?format=json")
    RateDto getRateByCode(@PathVariable("code") String code) throws IOException;

    @RequestMapping(method = RequestMethod.GET, value = "/rates/a/{code}/{date}?format=json")
    RateDto getRateByCodeAndDate(@PathVariable("code") String code, @PathVariable("date") String date) throws IOException;

    @RequestMapping(method = RequestMethod.GET, value = "/tables/a/{date}?format=json")
    TableDto[] getRatesByDate(@PathVariable("date") String date) throws IOException;
}
