package pl.ark.chr.buginator.data;

import pl.ark.chr.buginator.domain.enums.ErrorSeverity;
import pl.ark.chr.buginator.domain.enums.ErrorStatus;

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
    private List<ExternalStackTrace> stackTrace;

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

    public List<ExternalStackTrace> getStackTrace() {
        return stackTrace;
    }

    public void setStackTrace(List<ExternalStackTrace> stackTrace) {
        this.stackTrace = stackTrace;
    }

    public class ExternalStackTrace {
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
                ", stackTrace=" + stackTrace.stream().map(Object::toString).collect(Collectors.joining(",")) +
                '}';
    }
}
