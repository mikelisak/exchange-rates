package task.exchangerates.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import task.exchangerates.health.AppHealthIndicator;
import task.exchangerates.health.RabbitMQEventCountHealthIndicator;
import task.exchangerates.health.dto.HealthCheck;
import task.exchangerates.model.entity.Rate;
import task.exchangerates.service.NbpApiService;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
/**
 *
 * Fetches data from "https://api.nbp.pl/ and refreshes cache
 * @return Rate
 *
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/rates")
@Validated
public class RateController {

    private final NbpApiService nbpApiService;
    private final RabbitMQEventCountHealthIndicator myHealthIndicator;
    private final AppHealthIndicator appHealthIndicator;

    // Endpoint for returning rates for a specific date
    @GetMapping
    public ResponseEntity<List<Rate>> getAllRates(@RequestParam(required = false) LocalDate date) throws IOException {
        return ResponseEntity.ok(nbpApiService.getRatesByDate(date));
    }

    // Endpoint for getting rates for a specific currency
    @GetMapping("/code/{id}")
    public ResponseEntity<Rate> getRatesByCurrency(@PathVariable String id, @RequestParam(required = false) LocalDate date) throws IOException {
        return ResponseEntity.ok(nbpApiService.getRateByCurrency(id, date));
    }

    // Endpoint for refreshing cache for a specific currency
    @GetMapping("/refresh-cache/{code}")
    public ResponseEntity refreshCacheForCurrency(@PathVariable String code) throws IOException {
        nbpApiService.refreshCacheForCurrency(code);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/health")
    public ResponseEntity<HealthCheck> healthCheck() {
        return ResponseEntity.ok(
                HealthCheck.builder()
                .appHealth(appHealthIndicator.health().getStatus())
                .rabbitMQHealth(myHealthIndicator.health())
                        .build());
    }
}
