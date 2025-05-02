package currency.exchange.controllers;

import currency.exchange.dao.CurrencyDAO;
import currency.exchange.models.Currency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:63342")
public class CurrencyController {
    private final CurrencyDAO currencyDAO;

    @Autowired
    public CurrencyController(CurrencyDAO currencyDAO) {
        this.currencyDAO = currencyDAO;
    }

    @GetMapping("/currencies")
    public List<Currency> getAllCurrencies() {
        return currencyDAO.selectAllCurrencies();
    }

    @GetMapping("/currency/{code}")
    public ResponseEntity<?> getCurrencyByCode(@PathVariable("code") String code) {
        try {
            if (code == null || code.length() != 3) {
                return ResponseEntity.badRequest().body("Код должен быть длиной в 3 символа");
            }
            Currency currency = currencyDAO.selectCurrencyByCode(code);
            if (currency == null) { return ResponseEntity.notFound().build(); }

            return ResponseEntity.ok(currency);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping(path = "/currencies", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<?> addCurrency(@RequestParam("code") String code,
                                         @RequestParam("name") String name,
                                         @RequestParam("sign") String sign) {
        try {
            if (code == null || name == null || sign == null) {
                return ResponseEntity.badRequest().body("Пропущено необходимое поле");
            }
            if (code.length() != 3) {
                return ResponseEntity.badRequest().body("Код должен быть длиной в 3 символа");
            }

            Currency currency = new Currency();
            currency.setCode(code.toUpperCase());
            currency.setName(name);
            currency.setSign(sign);

            currencyDAO.saveCurrency(currency);

            Currency createdCurrency = currencyDAO.selectCurrencyByCode(currency.getCode());
            return ResponseEntity.status(HttpStatus.CREATED).body(createdCurrency);
        } catch (DuplicateKeyException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
