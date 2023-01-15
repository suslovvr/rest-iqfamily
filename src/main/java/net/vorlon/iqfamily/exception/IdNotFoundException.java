package net.vorlon.iqfamily.exception;

public class IdNotFoundException extends RuntimeException {
    private static final String MESSAGE = "Entity not found!";

    public IdNotFoundException(Long id) {
        super(String.format("Запись с идентификатором <%s> не найдена!", id));
    }

    public IdNotFoundException() {
        super(MESSAGE);
    }

    public IdNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public IdNotFoundException(Throwable cause) {
        super(MESSAGE, cause);
    }
}
