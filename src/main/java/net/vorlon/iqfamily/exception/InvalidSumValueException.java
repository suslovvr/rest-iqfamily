package net.vorlon.iqfamily.exception;

import java.math.BigDecimal;

public class InvalidSumValueException extends RuntimeException {
    public InvalidSumValueException(String msg, BigDecimal amount){
        super(String.format(msg,amount));
    }
}
