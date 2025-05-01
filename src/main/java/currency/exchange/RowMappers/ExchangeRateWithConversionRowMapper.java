package currency.exchange.RowMappers;

import currency.exchange.models.ExchangeRateWithConversion;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class ExchangeRateWithConversionRowMapper implements RowMapper<ExchangeRateWithConversion> {
    @Override
    public ExchangeRateWithConversion mapRow(ResultSet rs, int rowNum) throws SQLException {
        ExchangeRateWithConversion result = new ExchangeRateWithConversion();

        result.setId(rs.getInt("id"));
        result.setRate(rs.getBigDecimal("rate"));
        result.setAmount(rs.getBigDecimal("amount"));
        result.setConvertedAmount(rs.getBigDecimal("convertedAmount"));

        result.setBaseCurrency(ExchangeRateRowMapper.mapRowToCurrency(rs, "base_"));
        result.setTargetCurrency(ExchangeRateRowMapper.mapRowToCurrency(rs, "target_"));

        return result;
    }
}