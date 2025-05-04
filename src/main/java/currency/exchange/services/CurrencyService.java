package currency.exchange.services;

import currency.exchange.dao.CurrencyDAO;
import currency.exchange.models.Currency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class CurrencyService {
    private final CurrencyDAO currencyDAO;

    @Autowired
    public CurrencyService(CurrencyDAO currencyDAO) {
        this.currencyDAO = currencyDAO;
    }

    public List<Currency> getAllCurrencies() {
        return currencyDAO.selectAllCurrencies();
    }

    public ResponseEntity<Currency> getCurrencyByCode(String code) {
        try {
            if (code == null || code.length() != 3) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Поле пустое или код валюты не состоит из 3 символовов"
                );
            }

            Currency currency = currencyDAO.selectCurrencyByCode(code);
            if (currency == null) {
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Валюта не найдена"
                );
            }

            return ResponseEntity.status(HttpStatus.OK).body(currency);
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Ошибка базы данных"
            );
        }
    }

    public ResponseEntity<Currency> addCurrency(String code, String name, String sign) {
        try {
            if (code == null || name == null || sign == null) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Пропущено необходимое поле"
                );
            }
            if (code.length() != 3) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Код валюты должен состоять из 3 символов"
                );
            }

            Currency currency = new Currency();
            currency.setCode(code.toUpperCase());
            currency.setName(name);
            currency.setSign(sign);

            currencyDAO.saveCurrency(currency);

            Currency createdCurrency = currencyDAO.selectCurrencyByCode(currency.getCode());
            return ResponseEntity.status(HttpStatus.CREATED).body(createdCurrency);
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
}
