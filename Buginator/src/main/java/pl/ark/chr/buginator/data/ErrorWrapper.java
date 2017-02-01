package pl.ark.chr.buginator.data;

import pl.ark.chr.buginator.domain.Error;
import pl.ark.chr.buginator.domain.ErrorStackTrace;

import java.util.List;

/**
 * Created by Arek on 2017-02-01.
 */
public class ErrorWrapper {

    private final static String EXCEPTION_PREFIX = "Exception";
    private final static String CAUSED_BY_PREFIX = "Caused";

    private Error error;

    private String errorStackTrace;

    public ErrorWrapper(Error error) {
        this.error = error;
        this.errorStackTrace = generateStackTraceString(error.getStackTrace());
    }

    private String generateStackTraceString(List<ErrorStackTrace> stackTrace) {
        StringBuilder builder = new StringBuilder(300);
        stackTrace.forEach(t -> {
            if(t != null) {
                if (t.getStackTrace().startsWith(EXCEPTION_PREFIX) ||
                        t.getStackTrace().startsWith(CAUSED_BY_PREFIX)) {
                    builder.append(t.getStackTrace()).append("\n");
                } else {
                    builder.append("\t").append(t.getStackTrace()).append("\n");
                }
            }
        });

        return builder.toString();
    }

    public Error getError() {
        return error;
    }

    public String getErrorStackTrace() {
        return errorStackTrace;
    }
}
