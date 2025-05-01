package currency.exchange.dao;

import currency.exchange.components.SQLQuery;
import currency.exchange.models.ExchangeRate;
import currency.exchange.models.ExchangeRateWithConversion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ExchangeRateDAO {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<ExchangeRate> exchangeRateRowMapper;
    private final RowMapper<ExchangeRateWithConversion> exchangeRateWithConversionRowMapper;

    @Autowired
    public ExchangeRateDAO(JdbcTemplate jdbcTemplate,
                           RowMapper<ExchangeRate> exchangeRateRowMapper,
                           RowMapper<ExchangeRateWithConversion> exchangeRateWithConversionRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.exchangeRateRowMapper = exchangeRateRowMapper;
        this.exchangeRateWithConversionRowMapper = exchangeRateWithConversionRowMapper;
    }

    public List<ExchangeRate> selectAllExchangeRates () {
        return jdbcTemplate.query(SQLQuery.SELECT_ALL_EXCHANGE_RATES.getQuery(), exchangeRateRowMapper);
    }

    public ExchangeRate selectExchangeRateByCodes(String baseCode, String targetCode) {
        return jdbcTemplate.queryForObject(SQLQuery.SELECT_EXCHANGE_RATE_BY_CODES.getQuery(),
                new Object[] {baseCode, targetCode}, exchangeRateRowMapper);
    }

    public void insertExchangeRate (String baseCode, String targetCode, String rate) {
        jdbcTemplate.update("INSERT INTO exchangeRates(baseCurrencyId, targetCurrencyId, rate) VALUES (?, ?, ?)",
                getIdByCode(baseCode), getIdByCode(targetCode), rate);
    }

    public void updateExchangeRate (String baseCode, String targetCode, String rate) {
        jdbcTemplate.update("UPDATE exchangeRates SET rate = ? WHERE baseCurrencyId = ? AND targetCurrencyId = ?",
                rate, getIdByCode(baseCode), getIdByCode(targetCode));
    }

    public ExchangeRate selectExchangeRateWithValue (String baseCode, String targetCode, String amount) {
        return jdbcTemplate.queryForObject(SQLQuery.SELECT_EXCHANGE_RATE_BY_CODES_WITH_VALUE.getQuery(),
                new Object[] {amount, amount, baseCode, targetCode}, exchangeRateWithConversionRowMapper);
    }

    private Integer getIdByCode(String code) {
        return jdbcTemplate.queryForObject(SQLQuery.SELECT_ID_FROM_CURRENCIES.getQuery(), Integer.class, code);
    }
}
