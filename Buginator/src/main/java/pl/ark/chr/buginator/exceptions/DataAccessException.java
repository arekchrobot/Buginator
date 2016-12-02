package pl.ark.chr.buginator.exceptions;

import org.springframework.http.HttpStatus;

/**
 * Created by Arek on 2016-12-01.
 */
public class DataAccessException extends RestException {

    public DataAccessException(String message, String originalUrl) {
        super(message, HttpStatus.FORBIDDEN, originalUrl);
    }

    public DataAccessException(String message, String originalUrl, Object requestBody) {
        super(message, HttpStatus.FORBIDDEN, originalUrl, requestBody);
    }
}
