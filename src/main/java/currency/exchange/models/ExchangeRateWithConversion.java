package currency.exchange.models;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ExchangeRateWithConversion extends ExchangeRate {
    private BigDecimal amount;
    private BigDecimal convertedAmount;
}
