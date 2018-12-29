package pl.ark.chr.buginator.data;

import pl.ark.chr.buginator.domain.core.ErrorSeverity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Arek on 2017-04-01.
 */
public class ExternalData {

    private String applicationName;
    private String errorTitle;
    private String errorDescription;
    private ErrorSeverity errorSeverity;
    private String userAgentString;
    private String dateTimeString;
    private String dateString;
    private String requestUrl;
    private String queryParams;
    private String requestMethod;
    private List<ExternalStackTrace> stackTrace;
    private List<String> requestParams;
    private List<String> requestHeaders;

    public ExternalData() {
        this.stackTrace = new ArrayList<>();
        this.requestParams = new ArrayList<>();
        this.requestHeaders = new ArrayList<>();
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getErrorTitle() {
        return errorTitle;
    }

    public void setErrorTitle(String errorTitle) {
        this.errorTitle = errorTitle;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }

    public ErrorSeverity getErrorSeverity() {
        return errorSeverity;
    }

    public void setErrorSeverity(ErrorSeverity errorSeverity) {
        this.errorSeverity = errorSeverity;
    }

    public String getUserAgentString() {
        return userAgentString;
    }

    public void setUserAgentString(String userAgentString) {
        this.userAgentString = userAgentString;
    }

    public String getDateTimeString() {
        return dateTimeString;
    }

    public void setDateTimeString(String dateTimeString) {
        this.dateTimeString = dateTimeString;
    }

    public String getDateString() {
        return dateString;
    }

    public void setDateString(String dateString) {
        this.dateString = dateString;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public String getQueryParams() {
        return queryParams;
    }

    public void setQueryParams(String queryParams) {
        this.queryParams = queryParams;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public List<ExternalStackTrace> getStackTrace() {
        return stackTrace;
    }

    public void setStackTrace(List<ExternalStackTrace> stackTrace) {
        this.stackTrace = stackTrace;
    }

    public List<String> getRequestParams() {
        return requestParams;
    }

    public void setRequestParams(List<String> requestParams) {
        this.requestParams = requestParams;
    }

    public List<String> getRequestHeaders() {
        return requestHeaders;
    }

    public void setRequestHeaders(List<String> requestHeaders) {
        this.requestHeaders = requestHeaders;
    }

    public static class ExternalStackTrace {
        private int order;
        private String description;

        public int getOrder() {
            return order;
        }

        public void setOrder(int order) {
            this.order = order;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        @Override
        public String toString() {
            return "ExternalStackTrace{" +
                    "order=" + order +
                    ", description='" + description + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "ExternalData{" +
                "applicationName='" + applicationName + '\'' +
                ", errorTitle='" + errorTitle + '\'' +
                ", errorDescription='" + errorDescription + '\'' +
                ", errorSeverity=" + errorSeverity +
                ", userAgentString='" + userAgentString + '\'' +
                ", dateTimeString='" + dateTimeString + '\'' +
                ", dateString='" + dateString + '\'' +
                ", requestUrl='" + requestUrl + '\'' +
                ", queryParams='" + queryParams + '\'' +
                ", requestMethod='" + requestMethod + '\'' +
                ", stackTrace=" + stackTrace.stream().map(Object::toString).collect(Collectors.joining(",")) +
                ", requestHeaders=" + requestHeaders.stream().collect(Collectors.joining(",")) +
                ", requestParams=" + requestParams.stream().collect(Collectors.joining(",")) +
                '}';
    }
}
