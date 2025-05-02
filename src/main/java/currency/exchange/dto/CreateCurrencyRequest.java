package currency.exchange.dto;

import lombok.Getter;

@Getter
public class CreateCurrencyRequest {
    private String name;
    private String code;
    private String sign;
}
