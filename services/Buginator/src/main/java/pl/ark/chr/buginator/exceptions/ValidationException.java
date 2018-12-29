package pl.ark.chr.buginator.exceptions;

/**
 * Created by Arek on 2016-10-21.
 */
public class ValidationException extends Exception {

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
