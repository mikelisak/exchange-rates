package task.exchangerates.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import task.exchangerates.model.entity.Rate;
import task.exchangerates.service.NbpApiService;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/rate")
@Validated
public class RateController {

    private final NbpApiService nbpApiService;

    @GetMapping("/rates")
    public ResponseEntity<List<Rate>> getAllRates(@RequestParam(required = false) LocalDate date) {
        if (date != null) {
            return ResponseEntity.ok(nbpApiService.getRatesForDate(date));
        }
        return ResponseEntity.ok(nbpApiService.getAllRatesForToday());
    }

    @GetMapping("/rates/{id}")
    public ResponseEntity<Rate> getRatesByCurrency(@PathVariable String id) {
            return ResponseEntity.ok(nbpApiService.getRatesForCurrency(id));
    }

    // Endpoint for refreshing cache for a specific currency
    @GetMapping("/refresh-cache")
    public void refreshCache(@RequestParam String code) {
        nbpApiService.refreshCacheForCurrency(code);
    }
}
