package currency.exchange.components;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Exceptions {
    CODE_MUST_BE_THREE_CHARACTERS ("Код должен быть длиной в 3 символа"),
    DATABASE_ERROR_IN_SERVICE("Ошибка связанная с обращением к базе с данными"),
    DATABASE_ERROR_IN_DAO("Ошибка базы данных"),
    REQUIRED_FIELD_OMITTED ("Пропущено необходимое поле");

    private final String exception;
}
