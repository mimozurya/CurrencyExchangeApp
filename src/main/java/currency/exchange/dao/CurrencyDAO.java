package currency.exchange.dao;

import currency.exchange.components.Exceptions;
import currency.exchange.components.SQLQuery;
import currency.exchange.models.Currency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CurrencyDAO {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public CurrencyDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Currency> selectAllCurrencies() {
        return jdbcTemplate.query(SQLQuery.SELECT_ALL_CURRENCIES.getQuery(), new BeanPropertyRowMapper<>(Currency.class));
    }

    public Currency selectCurrencyByCode(String code) {
        try {
            return jdbcTemplate.query(SQLQuery.SELECT_CURRENCY_BY_CODE.getQuery(), new Object[]{code},
                    new BeanPropertyRowMapper<>(Currency.class)).stream().findAny().orElse(null);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public void saveCurrency(Currency currency) {
        try {
            jdbcTemplate.update(SQLQuery.INSERT_INTO_CURRENCIES.getQuery(),
                    currency.getName(), currency.getCode(), currency.getSign());
        } catch (DuplicateKeyException e) {
            throw new DuplicateKeyException(Exceptions.DATABASE_ERROR_IN_DAO.getException());
        }
    }
}
