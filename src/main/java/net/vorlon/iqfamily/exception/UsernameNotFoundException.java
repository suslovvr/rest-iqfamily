package net.vorlon.iqfamily.exception;

public class UsernameNotFoundException extends RuntimeException {
    public UsernameNotFoundException(String msg){
        super(msg);
    }
}
