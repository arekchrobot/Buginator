package pl.ark.chr.buginator.app.exceptions;

import org.springframework.http.HttpStatus;

public class DataNotFoundException extends RestException {

    private static final long serialVersionUID = 921564370032260379L;

    public DataNotFoundException(String message) {
        this(message,  "");
    }

    public DataNotFoundException(String message, String originalUrl) {
        super(message, HttpStatus.NOT_FOUND, originalUrl);
    }

    public DataNotFoundException(String message, String originalUrl, Object requestBody) {
        super(message, HttpStatus.NOT_FOUND, originalUrl, requestBody);
    }
}
