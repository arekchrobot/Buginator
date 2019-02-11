package pl.ark.chr.buginator.commons.exceptions;

public class PasswordTokenExpiredException extends RuntimeException {

    public PasswordTokenExpiredException(String message) {
        super(message);
    }
}
