package pl.ark.chr.buginator.auth.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.ark.chr.buginator.commons.dto.ErrorResponseDTO;
import pl.ark.chr.buginator.commons.exceptions.DuplicateException;

import javax.servlet.http.HttpServletRequest;
import java.util.stream.Collectors;

@ControllerAdvice
public class ExceptionHandlerConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionHandlerConfig.class);

    @ExceptionHandler(DuplicateException.class)
    public ResponseEntity<ErrorResponseDTO> handleDuplicateException(DuplicateException ex) {
        return handleError(ex, HttpStatus.BAD_REQUEST);
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

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponseDTO> handleRuntimeException(RuntimeException ex) {
        return handleError(ex, HttpStatus.INTERNAL_SERVER_ERROR);
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
