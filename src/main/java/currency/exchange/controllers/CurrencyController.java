package currency.exchange.controllers;

import currency.exchange.models.Currency;
import currency.exchange.services.CurrencyService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:63342")
public class CurrencyController {
    private final CurrencyService currencyService;

    public CurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @GetMapping("/currencies")
    public List<Currency> getAllCurrencies() {
        return currencyService.getAllCurrencies();
    }

    @GetMapping("/currency/{code}")
    public ResponseEntity<Currency> getCurrencyByCode(@PathVariable String code) {
        return currencyService.getCurrencyByCode(code);
    }

    @PostMapping(path = "/currencies", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Currency> addCurrency(@RequestParam String code,
                                                @RequestParam String name,
                                                @RequestParam String sign) {
        return currencyService.addCurrency(code, name, sign);
    }
}
