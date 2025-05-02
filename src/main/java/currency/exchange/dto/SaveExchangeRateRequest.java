package currency.exchange.dto;

import lombok.Getter;

@Getter
public class SaveExchangeRateRequest {
    private String baseCurrencyCode;
    private String targetCurrencyCode;
    private String rate;
}
