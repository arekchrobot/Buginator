package pl.ark.chr.buginator.app.exceptions;

/**
 * Created by Arek on 2017-02-27.
 */
public class TokenNotExistException extends RuntimeException {

    public TokenNotExistException(String message) {
        super(message);
    }

    public TokenNotExistException(String message, Throwable cause) {
        super(message, cause);
    }
}
