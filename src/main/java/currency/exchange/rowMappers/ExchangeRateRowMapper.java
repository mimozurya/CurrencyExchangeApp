package currency.exchange.rowMappers;

import currency.exchange.models.Currency;
import currency.exchange.models.ExchangeRate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class ExchangeRateRowMapper implements RowMapper<ExchangeRate> {
    @Override
    public ExchangeRate mapRow(ResultSet rs, int rowNum) throws SQLException {
        ExchangeRate exchangeRate = new ExchangeRate();
        exchangeRate.setId(rs.getInt("id"));
        exchangeRate.setRate(rs.getBigDecimal("rate"));

        exchangeRate.setBaseCurrency(mapRowToCurrency(rs, "base_"));
        exchangeRate.setTargetCurrency(mapRowToCurrency(rs, "target_"));

        return exchangeRate;
    }

    public static Currency mapRowToCurrency(ResultSet rs, String prefix) throws SQLException {
        Currency currency = new Currency();
        currency.setId(rs.getInt(prefix + "id"));
        currency.setCode(rs.getString(prefix + "code"));
        currency.setName(rs.getString(prefix + "name"));
        currency.setSign(rs.getString(prefix + "sign"));
        return currency;
    }
}
