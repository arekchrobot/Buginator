package pl.ark.chr.buginator.app.error;

import org.apache.commons.text.StringEscapeUtils;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import pl.ark.chr.buginator.commons.util.DtoMapper;
import pl.ark.chr.buginator.domain.core.ErrorStackTrace;
import pl.ark.chr.buginator.domain.core.Error;

import java.util.List;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = "spring", unmappedTargetPolicy = IGNORE, uses = {UserAgentMapper.class},
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class ErrorDetailsMapper implements DtoMapper<Error, ErrorDetailsDTO> {

    private final static String EXCEPTION_PREFIX = "Exception";
    private final static String CAUSED_BY_PREFIX = "Caused";

    @Override
    @Mapping(target = "stackTrace", expression = "java(buildStackTrace(error.getStackTrace()))")
    @Mapping(target = "requestHeaders", expression = "java(unescapeRequestHeaders(error.getRequestHeaders()))")
    @Mapping(target = "requestParams", expression = "java(unescapeRequestParams(error.getRequestParams()))")
    public abstract ErrorDetailsDTO toDto(Error error);

    protected String buildStackTrace(List<ErrorStackTrace> stackTrace) {
        StringBuilder builder = new StringBuilder(300);
        stackTrace.forEach(t -> {
            if (t != null) {
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

    @Named("requestHeaders")
    protected String unescapeRequestHeaders(String requestHeaders) {
        return StringEscapeUtils.unescapeJava(requestHeaders);
    }

    @Named("requestParams")
    protected String unescapeRequestParams(String requestParams) {
        return StringEscapeUtils.unescapeJava(requestParams);
    }
}
