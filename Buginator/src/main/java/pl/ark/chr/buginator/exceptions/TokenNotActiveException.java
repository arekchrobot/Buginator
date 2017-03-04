package pl.ark.chr.buginator.exceptions;

/**
 * Created by Arek on 2017-02-27.
 */
public class TokenNotActiveException extends RuntimeException {

    public TokenNotActiveException(String message) {
        super(message);
    }

    public TokenNotActiveException(String message, Throwable cause) {
        super(message, cause);
    }
}
