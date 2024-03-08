package task.exchangerates.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import task.exchangerates.model.entity.Rate;
import task.exchangerates.service.NbpApiService;

import java.io.IOException;
import java.net.MalformedURLException;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/rate")
@Validated
public class RateController {

    private final NbpApiService nbpApiService;

    // Endpoint for returning rates for a specific date
    @GetMapping
    public ResponseEntity<List<Rate>> getAllRates(@RequestParam(required = false) LocalDate date) throws IOException {
        return ResponseEntity.ok(nbpApiService.getAllRates(date));
    }

    // Endpoint for getting rates for a specific currency
    @GetMapping("/{id}")
    public ResponseEntity<Rate> getRatesByCurrency(@PathVariable String id) throws MalformedURLException {
        return ResponseEntity.ok(nbpApiService.getRatesForCurrency(id));
    }

    // Endpoint for refreshing cache for a specific currency
    @GetMapping("/refresh-cache/{code}")
    public void refreshCache(@PathVariable String code) throws MalformedURLException {
        nbpApiService.refreshCacheForCurrency(code);
    }
}
