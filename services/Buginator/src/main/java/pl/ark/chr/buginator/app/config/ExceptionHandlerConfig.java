package pl.ark.chr.buginator.app.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.ark.chr.buginator.app.exceptions.RestException;
import pl.ark.chr.buginator.commons.dto.ErrorResponseDTO;
import pl.ark.chr.buginator.util.ExceptionWrapper;
import pl.ark.chr.buginator.util.HttpUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Created by Arek on 2016-09-28.
 */
//TODO: Cleanup
@ControllerAdvice
public class ExceptionHandlerConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionHandlerConfig.class);

    @Autowired
    private MessageSource messageSource;

    //TODO: Return ErrorResponseDTO
    @ExceptionHandler(value = {RestException.class})
    public ResponseEntity<ExceptionWrapper> restErrorHandler(HttpServletRequest request, RestException e) {
        String loggerMsg = new StringBuffer()
                .append("Error executing url: ")
                .append(e.getOriginalUrl())
                .append(e.getRequestBody() != null ? " with request body: " + e.getRequestBody().toString() : "")
                .append(" .With exception: ")
                .append(e.toString())
                .toString();
        LOGGER.info(loggerMsg);
        ExceptionWrapper error = new ExceptionWrapper(e.getStatus().value(), e.getLocalizedMessage());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        return new ResponseEntity<>(error, headers, HttpStatus.valueOf(error.getStatus()));
    }

    //TODO: Return ErrorResponseDTO
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
            LOGGER.info(loggerMsg, e);
        } catch (IOException ex) {
            LOGGER.info("No post body found for exception.");
        }


        ExceptionWrapper error = new ExceptionWrapper(HttpStatus.INTERNAL_SERVER_ERROR.value(), messageSource.getMessage("internalError.msg", null, locale));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        return new ResponseEntity<>(error, headers, HttpStatus.valueOf(error.getStatus()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidationException(MethodArgumentNotValidException ex,
                                                                      HttpServletRequest httpServletRequest) {
        String errorMessage = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(","));

        LOGGER.error("Exception executing " + httpServletRequest.getMethod() + " url: "
                + httpServletRequest.getRequestURL().toString());

        return handleError(ex, HttpStatus.BAD_REQUEST, errorMessage);
    }

    private ResponseEntity<ErrorResponseDTO> handleError(Exception ex, HttpStatus status) {
        return handleError(ex, status, ex.getMessage());
    }

    private ResponseEntity<ErrorResponseDTO> handleError(Exception ex, HttpStatus status, String message) {
        LOGGER.error("Exception occurred with message: " + message, ex);

        var errorResponseDTO = new ErrorResponseDTO(message);
        var httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
        return new ResponseEntity<>(errorResponseDTO, httpHeaders, status);
    }
}
