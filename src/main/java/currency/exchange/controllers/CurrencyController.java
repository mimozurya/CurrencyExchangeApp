package currency.exchange.controllers;

import currency.exchange.dao.CurrencyDAO;
import currency.exchange.models.Currency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
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
    public Currency getCurrencyByCode(@PathVariable("code") String code) {
        return currencyDAO.selectCurrencyByCode(code);
    }

    @PostMapping(path = "/currencies", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Currency> addCurrency(Currency currency) {
        currencyDAO.saveCurrency(currency);
        return new ResponseEntity<>(currencyDAO.selectCurrencyByCode(currency.getCode()), HttpStatus.CREATED);
    }
}
