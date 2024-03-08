package task.exchangerates.service;

import task.exchangerates.model.entity.Rate;

import java.io.IOException;
import java.net.MalformedURLException;
import java.time.LocalDate;
import java.util.List;


public interface NbpServiceApiClient {
    Rate getRatesByCurrency(String code) throws MalformedURLException;

    List<Rate> getRatesByDate(LocalDate date) throws IOException;

    void refreshCacheForCurrency(String code) throws MalformedURLException;

}
