package pl.ark.chr.buginator.app.exceptions;

import org.springframework.http.HttpStatus;

public class DataAccessException extends RestException {

    private static final long serialVersionUID = 4809651147043471877L;

    public DataAccessException(String message, String originalUrl) {
        super(message, HttpStatus.FORBIDDEN, originalUrl);
    }

    public DataAccessException(String message, String originalUrl, Object requestBody) {
        super(message, HttpStatus.FORBIDDEN, originalUrl, requestBody);
    }
}
