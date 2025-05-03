package currency.exchange.services;

import currency.exchange.components.Exceptions;
import currency.exchange.dao.CurrencyDAO;
import currency.exchange.models.Currency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CurrencyService {
    private final CurrencyDAO currencyDAO;

    @Autowired
    public CurrencyService(CurrencyDAO currencyDAO) {
        this.currencyDAO = currencyDAO;
    }

    public List<Currency> getAllCurrencies() {
        return currencyDAO.selectAllCurrencies();
    }

    public ResponseEntity<?> getCurrencyByCode(String code) {
        try {
            if (code == null || code.length() != 3) {
                return ResponseEntity.badRequest().body(Exceptions.CODE_MUST_BE_THREE_CHARACTERS.getException());
            }
            Currency currency = currencyDAO.selectCurrencyByCode(code);
            if (currency == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.status(HttpStatus.OK).body(currency);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Exceptions.DATABASE_ERROR_IN_SERVICE.getException());
        }
    }

    public ResponseEntity<?> addCurrency(String code, String name, String sign) {
        try {
            if (code == null || name == null || sign == null) {
                return ResponseEntity.badRequest().body(Exceptions.REQUIRED_FIELD_OMITTED.getException());
            }
            if (code.length() != 3) {
                return ResponseEntity.badRequest().body(Exceptions.CODE_MUST_BE_THREE_CHARACTERS.getException());
            }

            Currency currency = new Currency();
            currency.setCode(code.toUpperCase());
            currency.setName(name);
            currency.setSign(sign);

            currencyDAO.saveCurrency(currency);

            Currency createdCurrency = currencyDAO.selectCurrencyByCode(currency.getCode());
            return ResponseEntity.status(HttpStatus.CREATED).body(createdCurrency);
        } catch (DuplicateKeyException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Exceptions.DATABASE_ERROR_IN_SERVICE.getException());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Exceptions.DATABASE_ERROR_IN_SERVICE.getException());
        }
    }
}
