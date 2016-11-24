package pl.ark.chr.buginator.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.ark.chr.buginator.exceptions.RestException;
import pl.ark.chr.buginator.util.ExceptionWrapper;
import pl.ark.chr.buginator.util.HttpUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Created by Arek on 2016-09-28.
 */
@ControllerAdvice
public class RestExceptionHandler {

    private Logger logger = LoggerFactory.getLogger(RestExceptionHandler.class);

    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(value = {RestException.class})
    public ResponseEntity<ExceptionWrapper> restErrorHandler(HttpServletRequest request, RestException e) {
        String loggerMsg = new StringBuffer()
                .append("Error executing url: ")
                .append(e.getOriginalUrl())
                .append(e.getRequestBody() != null ? " with request body: " + e.getRequestBody().toString() : "")
                .append(".With exception: ")
                .append(e.toString())
                .toString();
        logger.info(loggerMsg);
        ExceptionWrapper error = new ExceptionWrapper(e.getStatus().value(), e.getLocalizedMessage());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        return new ResponseEntity<>(error, headers, HttpStatus.valueOf(error.getStatus()));
    }

    @ExceptionHandler(value = {RuntimeException.class})
    public ResponseEntity<ExceptionWrapper> runtimeErrorHandler(HttpServletRequest request, Locale locale, RuntimeException e) {
        try {
            String loggerMsg = new StringBuffer()
                    .append("Error executing url: ")
                    .append(HttpUtil.generateOriginalUrl(request))
                    .append("POST".equalsIgnoreCase(request.getMethod()) ? request.getReader().lines().collect(Collectors.joining(System.lineSeparator())) : "")
                    .append(".With exception: ")
                    .append(e.toString())
                    .toString();
            logger.info(loggerMsg);
        } catch (IOException ex) {
            logger.info("No post body found for exception.");
        }


        ExceptionWrapper error = new ExceptionWrapper(HttpStatus.INTERNAL_SERVER_ERROR.value(), messageSource.getMessage("internalError.msg", null, locale));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        return new ResponseEntity<>(error, headers, HttpStatus.valueOf(error.getStatus()));
    }
}
