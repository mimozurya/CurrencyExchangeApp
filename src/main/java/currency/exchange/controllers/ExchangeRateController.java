package currency.exchange.controllers;

import currency.exchange.dao.ExchangeRateDAO;
import currency.exchange.models.ExchangeRate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:63342")
public class ExchangeRateController {
    private final ExchangeRateDAO exchangeRateDAO;

    @Autowired
    public ExchangeRateController(ExchangeRateDAO exchangeRateDAO) {
        this.exchangeRateDAO = exchangeRateDAO;
    }

    @GetMapping("/exchangeRates")
    public ResponseEntity<List<ExchangeRate>> getAllExchangeRates() {
        return new ResponseEntity<>(exchangeRateDAO.selectAllExchangeRates(), HttpStatus.OK);
    }

    @GetMapping("/exchangeRate/{currencyPair}")
    public ResponseEntity<?> getExchangeRateByCode(@PathVariable("currencyPair") String currencyPair) {
        String baseCode = currencyPair.substring(0, 3).toUpperCase();
        String targetCode = currencyPair.substring(3).toUpperCase();

        try {
            if (baseCode.length() != 3 || targetCode.length() != 3) {
                return ResponseEntity.badRequest().body("Код должен быть длиной в 3 символа");
            }

            return new ResponseEntity<>(exchangeRateDAO.selectExchangeRateByCodes(baseCode, targetCode), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping(path = "/exchangeRates", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<?> saveExchangeRate(@RequestParam("baseCurrencyCode") String baseCurrencyCode,
                                              @RequestParam("targetCurrencyCode") String targetCurrencyCode,
                                              @RequestParam("rate") String rate) {
        try {
            if (baseCurrencyCode == null || targetCurrencyCode == null || rate == null) {
                return ResponseEntity.badRequest().body("Пропущено необходимое поле");
            }
            if (baseCurrencyCode.length() != 3 || targetCurrencyCode.length() != 3) {
                return ResponseEntity.badRequest().body("Код должен быть длиной в 3 символа");
            }

            exchangeRateDAO.insertExchangeRate(baseCurrencyCode, targetCurrencyCode, rate);
            return new ResponseEntity<>(exchangeRateDAO.selectExchangeRateByCodes(baseCurrencyCode, targetCurrencyCode), HttpStatus.CREATED);
        } catch (DuplicateKeyException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping(value = "/exchangeRate/{currencyPair}", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<?> updateExchangeRate(@PathVariable("currencyPair") String currencyPair,
                                                @RequestParam("rate") String rate) {
        String baseCode = currencyPair.substring(0, 3).toUpperCase();
        String targetCode = currencyPair.substring(3).toUpperCase();

        try {
            if (baseCode.length() != 3 || targetCode.length() != 3) {
                return ResponseEntity.badRequest().body("Код должен быть длиной в 3 символа");
            }

            exchangeRateDAO.updateExchangeRate(baseCode, targetCode, rate);
            return new ResponseEntity<>(exchangeRateDAO.selectExchangeRateByCodes(baseCode, targetCode), HttpStatus.OK);
        } catch (DuplicateKeyException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

    }

    @GetMapping("/exchange")
    public ResponseEntity<?> getExchangeRateWithValue(@RequestParam("from") String baseCurrencyCode,
                                                      @RequestParam("to") String targetCurrencyCode,
                                                      @RequestParam("amount") String amount) {
        try {
            if (baseCurrencyCode == null || targetCurrencyCode == null || amount == null) {
                return ResponseEntity.badRequest().body("Пропущено необходимое поле");
            }
            if (baseCurrencyCode.length() != 3 || targetCurrencyCode.length() != 3) {
                return ResponseEntity.badRequest().body("Код должен быть длиной в 3 символа");
            }

            return new ResponseEntity<>(exchangeRateDAO.selectExchangeRateWithValue(baseCurrencyCode, targetCurrencyCode, amount), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
