package currency.exchange.services;

import currency.exchange.dao.ExchangeRateDAO;
import currency.exchange.models.ExchangeRate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ExchangeRateService {
    private final ExchangeRateDAO exchangeRateDAO;

    @Autowired
    public ExchangeRateService(ExchangeRateDAO exchangeRateDAO) {
        this.exchangeRateDAO = exchangeRateDAO;
    }

    public ResponseEntity<List<ExchangeRate>> getAllExchangeRates() {
        return new ResponseEntity<>(exchangeRateDAO.selectAllExchangeRates(), HttpStatus.OK);
    }

    public ResponseEntity<ExchangeRate> getExchangeRateByCode(String currencyPair) {
        String baseCode = currencyPair.substring(0, 3).toUpperCase();
        String targetCode = currencyPair.substring(3).toUpperCase();

        try {
            if (baseCode.length() != 3 || targetCode.length() != 3) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Код валюты не состоит из 3 символовов"
                );
            }

            return new ResponseEntity<>(exchangeRateDAO.selectExchangeRateByCodes(baseCode, targetCode), HttpStatus.OK);
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Ошибка базы данных"
            );
        }
    }

    public ResponseEntity<ExchangeRate> saveExchangeRate(String baseCurrencyCode, String targetCurrencyCode, String rate) {
        try {
            if (checkForEmptinessOfValues(baseCurrencyCode, targetCurrencyCode, rate)) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Пропущено необходимое поле"
                );
            }
            if (checkWrongNumberOfCharacters(baseCurrencyCode, targetCurrencyCode)) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Код валюты не состоит из 3 символовов"
                );
            }

            exchangeRateDAO.insertExchangeRate(baseCurrencyCode, targetCurrencyCode, rate);
            return new ResponseEntity<>(exchangeRateDAO.selectExchangeRateByCodes(baseCurrencyCode, targetCurrencyCode), HttpStatus.CREATED);
        } catch (DuplicateKeyException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_GATEWAY,
                    "Значение уже есть в базе данных"
            );
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Ошибка базы данных"
            );
        }
    }

    public ResponseEntity<ExchangeRate> updateExchangeRate(String currencyPair, String rate) {
        String baseCode = currencyPair.substring(0, 3).toUpperCase();
        String targetCode = currencyPair.substring(3).toUpperCase();

        try {
            if (baseCode.length() != 3 || targetCode.length() != 3) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Код валюты не состоит из 3 символовов"
                );
            }

            exchangeRateDAO.updateExchangeRate(baseCode, targetCode, rate);
            return new ResponseEntity<>(exchangeRateDAO.selectExchangeRateByCodes(baseCode, targetCode), HttpStatus.OK);
        } catch (DuplicateKeyException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_GATEWAY,
                    "Значение уже есть в базе данных"
            );
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Ошибка базы данных"
            );
        }
    }

    public ResponseEntity<?> getExchangeRateWithValue(String baseCurrencyCode, String targetCurrencyCode, String amount) {
        try {
            if (checkForEmptinessOfValues(baseCurrencyCode, targetCurrencyCode, amount)) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Пропущено необходимое поле"
                );
            }
            if (checkWrongNumberOfCharacters(baseCurrencyCode, targetCurrencyCode)) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Код валюты не состоит из 3 символовов"
                );
            }

            return new ResponseEntity<>(exchangeRateDAO.calculateExchangeRateWithValue(baseCurrencyCode, targetCurrencyCode, amount), HttpStatus.OK);
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Ошибка базы данных"
            );
        }
    }

    private boolean checkForEmptinessOfValues(String baseCode, String targetCode, String value) {
        return baseCode == null || targetCode == null || value == null;
    }

    private boolean checkWrongNumberOfCharacters(String baseCode, String targetCode) {
        return baseCode.length() != 3 || targetCode.length() != 3;
    }
}
