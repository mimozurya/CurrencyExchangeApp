package currency.exchange.dao;

import currency.exchange.models.ExchangeRate;
import currency.exchange.models.ExchangeRateWithConversion;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ExchangeRateDAO {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<ExchangeRate> exchangeRateRowMapper;
    private final RowMapper<ExchangeRateWithConversion> exchangeRateWithConversionRowMapper;

    public ExchangeRateDAO(JdbcTemplate jdbcTemplate,
                           RowMapper<ExchangeRate> exchangeRateRowMapper,
                           RowMapper<ExchangeRateWithConversion> exchangeRateWithConversionRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.exchangeRateRowMapper = exchangeRateRowMapper;
        this.exchangeRateWithConversionRowMapper = exchangeRateWithConversionRowMapper;
    }

    public List<ExchangeRate> selectAllExchangeRates() {
        return jdbcTemplate.query("""
                SELECT\s
                                er.id,\s
                                er.rate,\s
                                base.id AS base_id,\s
                                base.code AS base_code,\s
                                base.name AS base_name,\s
                                base.sign AS base_sign,\s
                                target.id AS target_id,\s
                                target.code AS target_code,\s
                                target.name AS target_name,\s
                                target.sign AS target_sign\s
                            FROM\s
                                exchangeRates er
                            JOIN\s
                                currencies base ON er.baseCurrencyId = base.id
                            JOIN\s
                                currencies target ON er.targetCurrencyId = target.id
                """, exchangeRateRowMapper);
    }

    public ExchangeRate selectExchangeRateByCodes(String baseCode, String targetCode) {
        return jdbcTemplate.queryForObject("""
                    SELECT\s
                                        er.id,\s
                                        er.rate,\s
                                        base.id AS base_id,\s
                                        base.code AS base_code,\s
                                        base.name AS base_name,\s
                                        base.sign AS base_sign,\s
                                        target.id AS target_id,\s
                                        target.code AS target_code,\s
                                        target.name AS target_name,\s
                                        target.sign AS target_sign\s
                                    FROM\s
                                        exchangeRates er
                                    JOIN\s
                                        currencies base ON er.baseCurrencyId = base.id
                                    JOIN\s
                                        currencies target ON er.targetCurrencyId = target.id\s
                                    WHERE base_code = ? and target_code = ?
                """, new Object[]{baseCode, targetCode}, exchangeRateRowMapper);

    }

    public void insertExchangeRate(String baseCode, String targetCode, String rate) {
        jdbcTemplate.update("INSERT INTO exchangeRates(baseCurrencyId, targetCurrencyId, rate) VALUES (?, ?, ?)",
                getIdByCode(baseCode), getIdByCode(targetCode), rate);

    }

    public void updateExchangeRate(String baseCode, String targetCode, String rate) {
        jdbcTemplate.update("UPDATE exchangeRates SET rate = ? WHERE baseCurrencyId = ? AND targetCurrencyId = ?",
                rate, getIdByCode(baseCode), getIdByCode(targetCode));

    }

    public ExchangeRate calculateExchangeRateWithValue(String baseCode, String targetCode, String amount) {
        return jdbcTemplate.queryForObject(
                """
                        SELECT\s
                            er.id,\s
                            er.rate,\s
                            ? AS amount,\s
                            (er.rate * ?) AS convertedAmount,\s
                            base.id AS base_id,\s
                            base.code AS base_code,\s
                            base.name AS base_name,\s
                            base.sign AS base_sign,\s
                            target.id AS target_id,\s
                            target.code AS target_code,\s
                            target.name AS target_name,\s
                            target.sign AS target_sign\s
                        FROM\s
                            exchangeRates er
                        JOIN\s
                            currencies base ON er.baseCurrencyId = base.id
                        JOIN\s
                            currencies target ON er.targetCurrencyId = target.id\s
                        WHERE base_code = ? and target_code = ?
                        """, new Object[]{amount, amount, baseCode, targetCode}, exchangeRateWithConversionRowMapper);

    }

    private Integer getIdByCode(String code) {
        return jdbcTemplate.queryForObject("select id from currencies where code = ?", Integer.class, code);
    }
}
