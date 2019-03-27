package pl.ark.chr.buginator.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class RestException extends RuntimeException {

    private static final long serialVersionUID = 7486292258238172906L;

    private HttpStatus status;
    private String originalUrl;
    private Object requestBody;

    public RestException(String message, HttpStatus status, String originalUrl) {
        super(message);
        this.status = status;
        this.originalUrl = originalUrl;
    }

    public RestException(String message, HttpStatus status, String originalUrl, Object requestBody) {
        super(message);
        this.status = status;
        this.originalUrl = originalUrl;
        this.requestBody = requestBody;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public Object getRequestBody() {
        return requestBody;
    }
}
