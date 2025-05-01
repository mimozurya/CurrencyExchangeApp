package currency.exchange.controllers;

import currency.exchange.dao.ExchangeRateDAO;
import currency.exchange.models.ExchangeRate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ExchangeRateController {
    private final ExchangeRateDAO exchangeRateDAO;

    @Autowired
    public ExchangeRateController(ExchangeRateDAO exchangeRateDAO) {
        this.exchangeRateDAO = exchangeRateDAO;
    }

    @GetMapping("/exchangeRates")
    public List<ExchangeRate> getAllExchangeRates() {
        return exchangeRateDAO.selectAllExchangeRates();
    }

    @GetMapping("/exchangeRate/{currencyPair}")
    public ExchangeRate getExchangeRateByCode(@PathVariable("currencyPair") String currencyPair) {
        String baseCode = currencyPair.substring(0, 3).toUpperCase();
        String targetCode = currencyPair.substring(3).toUpperCase();
        return exchangeRateDAO.selectExchangeRateByCodes(baseCode, targetCode);
    }

    @PostMapping(path = "/exchangeRates", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<ExchangeRate> saveExchangeRate(@RequestParam("baseCurrencyCode") String baseCurrencyCode,
                                                         @RequestParam("targetCurrencyCode") String targetCurrencyCode,
                                                         @RequestParam("rate") String rate) {
        exchangeRateDAO.insertExchangeRate(baseCurrencyCode, targetCurrencyCode, rate);
        return new ResponseEntity<>(exchangeRateDAO.selectExchangeRateByCodes(baseCurrencyCode, targetCurrencyCode), HttpStatus.CREATED);
    }

    @PatchMapping(path = "/exchangeRate/{currencyPair}", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<ExchangeRate> updateExchangeRate(@PathVariable("currencyPair") String currencyPair,
                                                           @RequestParam("rate") String rate) {
        String baseCode = currencyPair.substring(0, 3).toUpperCase();
        String targetCode = currencyPair.substring(3).toUpperCase();
        exchangeRateDAO.updateExchangeRate(baseCode, targetCode, rate);
        return new ResponseEntity<>(exchangeRateDAO.selectExchangeRateByCodes(baseCode, targetCode), HttpStatus.OK);
    }

    @GetMapping("/exchange")
    public ResponseEntity<ExchangeRate> getExchangeRateWithValue(@RequestParam("from") String baseCurrencyCode,
                                                                 @RequestParam("to") String targetCurrencyCode,
                                                                 @RequestParam("amount") String amount) {
        return new ResponseEntity<>(exchangeRateDAO.selectExchangeRateWithValue(baseCurrencyCode, targetCurrencyCode, amount), HttpStatus.OK);
    }
}
