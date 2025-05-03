package currency.exchange.services;

import currency.exchange.components.Exceptions;
import currency.exchange.dao.ExchangeRateDAO;
import currency.exchange.models.ExchangeRate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ExchangeRateService {
    private final ExchangeRateDAO exchangeRateDAO;

    @Autowired
    public ExchangeRateService(ExchangeRateDAO exchangeRateDAO) {
        this.exchangeRateDAO = exchangeRateDAO;
    }

    public ResponseEntity<List<ExchangeRate>> getAllExchangeRates() {
        return new ResponseEntity<>(exchangeRateDAO.selectAllExchangeRates(), HttpStatus.OK);
    }

    public ResponseEntity<?> getExchangeRateByCode(String currencyPair) {
        String baseCode = currencyPair.substring(0, 3).toUpperCase();
        String targetCode = currencyPair.substring(3).toUpperCase();

        try {
            if (baseCode.length() != 3 || targetCode.length() != 3) {
                return ResponseEntity.badRequest().body(Exceptions.CODE_MUST_BE_THREE_CHARACTERS.getException());
            }

            return new ResponseEntity<>(exchangeRateDAO.selectExchangeRateByCodes(baseCode, targetCode), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Exceptions.DATABASE_ERROR_IN_SERVICE.getException());
        }
    }

    public ResponseEntity<?> saveExchangeRate(String baseCurrencyCode, String targetCurrencyCode, String rate) {
        try {
            if (checkForEmptinessOfValues(baseCurrencyCode, targetCurrencyCode, rate)) {
                return ResponseEntity.badRequest().body(Exceptions.REQUIRED_FIELD_OMITTED.getException());
            }
            if (checkWrongNumberOfCharacters(baseCurrencyCode, targetCurrencyCode)) {
                return ResponseEntity.badRequest().body(Exceptions.CODE_MUST_BE_THREE_CHARACTERS.getException());
            }

            exchangeRateDAO.insertExchangeRate(baseCurrencyCode, targetCurrencyCode, rate);
            return new ResponseEntity<>(exchangeRateDAO.selectExchangeRateByCodes(baseCurrencyCode, targetCurrencyCode), HttpStatus.CREATED);
        } catch (DuplicateKeyException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Exceptions.DATABASE_ERROR_IN_SERVICE.getException());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Exceptions.DATABASE_ERROR_IN_SERVICE.getException());
        }
    }

    public ResponseEntity<?> updateExchangeRate(String currencyPair, String rate) {
        String baseCode = currencyPair.substring(0, 3).toUpperCase();
        String targetCode = currencyPair.substring(3).toUpperCase();

        try {
            if (baseCode.length() != 3 || targetCode.length() != 3) {
                return ResponseEntity.badRequest().body(Exceptions.CODE_MUST_BE_THREE_CHARACTERS.getException());
            }

            exchangeRateDAO.updateExchangeRate(baseCode, targetCode, rate);
            return new ResponseEntity<>(exchangeRateDAO.selectExchangeRateByCodes(baseCode, targetCode), HttpStatus.OK);
        } catch (DuplicateKeyException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Exceptions.DATABASE_ERROR_IN_SERVICE.getException());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Exceptions.DATABASE_ERROR_IN_SERVICE.getException());
        }
    }

    public ResponseEntity<?> getExchangeRateWithValue(String baseCurrencyCode, String targetCurrencyCode, String amount) {
        try {
            if (checkForEmptinessOfValues(baseCurrencyCode, targetCurrencyCode, amount)) {
                return ResponseEntity.badRequest().body(Exceptions.REQUIRED_FIELD_OMITTED.getException());
            }
            if (checkWrongNumberOfCharacters(baseCurrencyCode, targetCurrencyCode)) {
                return ResponseEntity.badRequest().body(Exceptions.CODE_MUST_BE_THREE_CHARACTERS.getException());
            }

            return new ResponseEntity<>(exchangeRateDAO.calculateExchangeRateWithValue(baseCurrencyCode, targetCurrencyCode, amount), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Exceptions.DATABASE_ERROR_IN_SERVICE.getException());
        }
    }

    private boolean checkForEmptinessOfValues(String baseCode, String targetCode, String value) {
        return baseCode == null || targetCode == null || value == null;
    }

    private boolean checkWrongNumberOfCharacters(String baseCode, String targetCode) {
        return baseCode.length() != 3 || targetCode.length() != 3;
    }
}
