package currency.exchange.controllers;

import currency.exchange.models.ExchangeRate;
import currency.exchange.services.ExchangeRateService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:63342")
public class ExchangeRateController {
    private final ExchangeRateService exchangeRateService;

    @Autowired
    public ExchangeRateController(ExchangeRateService exchangeRateService) {
        this.exchangeRateService = exchangeRateService;
    }

    @GetMapping("/exchangeRates")
    public ResponseEntity<List<ExchangeRate>> getAllExchangeRates() {
        return exchangeRateService.getAllExchangeRates();
    }

    @GetMapping("/exchangeRate/{currencyPair}")
    public ResponseEntity<?> getExchangeRateByCode(@PathVariable String currencyPair) {
        return exchangeRateService.getExchangeRateByCode(currencyPair);
    }

    @PostMapping(path = "/exchangeRates", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<?> saveExchangeRate(@RequestParam String baseCurrencyCode,
                                              @RequestParam String targetCurrencyCode,
                                              @RequestParam String rate) {
        return exchangeRateService.saveExchangeRate(baseCurrencyCode, targetCurrencyCode, rate);
    }

    @PostMapping(value = "/exchangeRate/{pair}", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<?> updateExchangeRate(@PathVariable String pair,
                                                @RequestParam ("rate") @Valid String rate) {
        return exchangeRateService.updateExchangeRate(pair, rate);
    }

    @GetMapping("/exchange")
    public ResponseEntity<?> getExchangeRateWithValue(@RequestParam("from") String baseCurrencyCode,
                                                      @RequestParam("to") String targetCurrencyCode,
                                                      @RequestParam("amount") String amount) {
        return exchangeRateService.getExchangeRateWithValue(baseCurrencyCode, targetCurrencyCode, amount);
    }
}
