package currency.exchange.controllers;

import currency.exchange.dao.CurrencyDAO;
import currency.exchange.models.Currency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
