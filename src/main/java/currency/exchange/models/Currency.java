package currency.exchange.models;

import jakarta.validation.constraints.*;

public class Currency {
    private int id;

    @NotBlank(message = "Name should not be empty")
    private String name;

    @NotBlank(message = "Code should not be empty and should be have 3 symbol")
    @Min(3) @Max(3)
    private String code;

    @NotBlank(message = "Sign should not be empty")
    private String sign;

    public Currency(int id, String name, String code, String sign) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.sign = sign;
    }

    public Currency() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
