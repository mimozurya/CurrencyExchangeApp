package currency.exchange.components;

public enum SQLQuery {
    SELECT_ALL_EXCHANGE_RATES(
            """
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
                    """
    ),
    SELECT_EXCHANGE_RATE_BY_CODES(
            """
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
                    """
    ),
    SELECT_EXCHANGE_RATE_BY_CODES_WITH_VALUE(
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
                    """
    ),
    SELECT_ID_FROM_CURRENCIES("select id from currencies where code = ?");

    private String query;

    SQLQuery(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }
}
