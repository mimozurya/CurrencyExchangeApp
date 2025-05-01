package currency.exchange.models;

import java.math.BigDecimal;

public class ExchangeRateWithConversion extends ExchangeRate {
    private BigDecimal amount;
    private BigDecimal convertedAmount;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getConvertedAmount() {
        return convertedAmount;
    }

    public void setConvertedAmount(BigDecimal convertedAmount) {
        this.convertedAmount = convertedAmount;
    }
}
