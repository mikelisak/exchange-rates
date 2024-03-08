package task.exchangerates.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import task.exchangerates.model.entity.Rate;
import task.exchangerates.service.NbpApiService;
import task.exchangerates.service.NbpServiceApiClient;

import java.io.IOException;
import java.net.MalformedURLException;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/rates")
@Validated
public class RateController {

    private final NbpServiceApiClient nbpApiService;

    // Endpoint for returning rates for a specific date
    @GetMapping
    public ResponseEntity<List<Rate>> getAllRates(@RequestParam(required = false) LocalDate date) throws IOException {
        return ResponseEntity.ok(nbpApiService.getRatesByDate(date));
    }

    // Endpoint for getting rates for a specific currency
    @GetMapping("/{id}")
    public ResponseEntity<Rate> getRatesByCurrency(@PathVariable String id) throws IOException {
        return ResponseEntity.ok(nbpApiService.getRatesByCurrency(id));
    }

    // Endpoint for refreshing cache for a specific currency
    @GetMapping("/refresh-cache/{code}")
    public void refreshCacheForCurrency(@PathVariable String code) throws IOException {
        nbpApiService.refreshCacheForCurrency(code);
    }
}
