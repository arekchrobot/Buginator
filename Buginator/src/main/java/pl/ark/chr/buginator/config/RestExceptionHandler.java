package pl.ark.chr.buginator.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.ark.chr.buginator.exceptions.RestException;
import pl.ark.chr.buginator.util.ExceptionWrapper;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Arek on 2016-09-28.
 */
@ControllerAdvice
public class RestExceptionHandler {

    private Logger logger = LoggerFactory.getLogger(RestExceptionHandler.class);

    @ExceptionHandler(value = {RestException.class})
    public ResponseEntity<ExceptionWrapper> restErrorHandler(HttpServletRequest request, RestException e) {
        String loggerMsg = new StringBuffer()
                .append("Error executing url: ")
                .append(e.getOriginalUrl())
                .append(e.getRequestBody() != null ? " with request body: " + e.getRequestBody().toString() : "")
                .toString();
        logger.info(loggerMsg);
        ExceptionWrapper error = new ExceptionWrapper(e.getStatus().value(), e.getMessage());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        return new ResponseEntity<>(error, headers, HttpStatus.valueOf(error.getStatus()));
    }
}
