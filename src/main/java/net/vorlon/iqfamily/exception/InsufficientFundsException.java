package net.vorlon.iqfamily.exception;

public class InsufficientFundsException extends RuntimeException {
    public InsufficientFundsException(String msg){
        super(msg);
    }
}
