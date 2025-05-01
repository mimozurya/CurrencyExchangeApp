package currency.exchange.dao;

import currency.exchange.models.Currency;
import org.springframework.beans.factory.annotation.Autowired;
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
        return jdbcTemplate.query("SELECT * FROM currencies", new BeanPropertyRowMapper<>(Currency.class));
    }

    public Currency selectCurrencyByCode(String code) {
        return jdbcTemplate.query("SELECT * FROM currencies WHERE code=?", new Object[]{code},
                        new BeanPropertyRowMapper<>(Currency.class)).stream().findAny().orElse(null);
    }

    public void saveCurrency(Currency currency) {
        jdbcTemplate.update("INSERT INTO currencies(name, code, sign) VALUES (?, ?, ?)",
                currency.getName(), currency.getCode(), currency.getSign());
    }
}
