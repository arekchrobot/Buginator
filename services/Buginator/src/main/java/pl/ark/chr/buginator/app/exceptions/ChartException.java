package pl.ark.chr.buginator.app.exceptions;

/**
 * Created by Arek on 2016-12-25.
 */
public class ChartException extends Exception {

    public ChartException(String message) {
        super(message);
    }

    public ChartException(String message, Throwable cause) {
        super(message, cause);
    }
}
