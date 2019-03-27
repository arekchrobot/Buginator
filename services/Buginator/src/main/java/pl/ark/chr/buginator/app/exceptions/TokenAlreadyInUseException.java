package pl.ark.chr.buginator.app.exceptions;

/**
 * Created by Arek on 2017-02-27.
 */
public class TokenAlreadyInUseException extends RuntimeException {

    public TokenAlreadyInUseException(String message) {
        super(message);
    }

    public TokenAlreadyInUseException(String message, Throwable cause) {
        super(message, cause);
    }
}
