package pl.ark.chr.buginator.ext.service.impl;

import net.pieroxy.ua.detection.UserAgentDetector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.ark.chr.buginator.data.ExternalData;
import pl.ark.chr.buginator.domain.core.Application;
import pl.ark.chr.buginator.domain.core.Error;
import pl.ark.chr.buginator.domain.core.ErrorStackTrace;
import pl.ark.chr.buginator.domain.core.UserAgentData;
import pl.ark.chr.buginator.domain.core.ErrorSeverity;
import pl.ark.chr.buginator.domain.core.ErrorStatus;
import pl.ark.chr.buginator.ext.service.ErrorResolver;
import pl.ark.chr.buginator.repository.core.ErrorRepository;
import pl.ark.chr.buginator.repository.core.UserAgentDataRepository;
import pl.ark.chr.buginator.util.ValidationUtil;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Arek on 2017-04-03.
 */
@Service
@Transactional
public class ErrorResolverImpl implements ErrorResolver {

    public static final Logger logger = LoggerFactory.getLogger(ErrorResolverImpl.class);

    private static final ErrorSeverity DEFAULT_SEVERITY = ErrorSeverity.ERROR;

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");


    @Autowired
    private ErrorRepository errorRepository;

    @Autowired
    private UserAgentDataRepository userAgentDataRepository;

    @Override
    public Error resolveError(ExternalData externalData, Application application) {
        validateEmptyString(externalData.getErrorTitle(), "Title");
        validateEmptyString(externalData.getErrorDescription(), "Description");

        if (externalData.getErrorSeverity() == null) {
            logger.warn("No severity found for external data: " + externalData.toString() + " and application: " + application.getId() + ". Setting default: " + DEFAULT_SEVERITY.name());
            externalData.setErrorSeverity(DEFAULT_SEVERITY);
        }

        List<Error> foundDuplicates = errorRepository.findDuplicateError(externalData.getErrorTitle(), externalData.getErrorDescription(),
                externalData.getErrorSeverity(), externalData.getRequestMethod(), externalData.getRequestUrl(), application);

        if (foundDuplicates.isEmpty()) {
            return createNewError(externalData, application);
        } else {
            return checkDuplicateOrCreate(externalData, application, foundDuplicates);
        }
    }

    private void validateEmptyString(String predicate, String key) {
        if (ValidationUtil.isBlank(predicate)) {
            logger.error(key + " is empty");
            throw new IllegalArgumentException(key + " is empty");
        }
    }

    private Error createNewError(ExternalData externalData, Application application) {
        Error error = Error.builder(externalData.getErrorTitle(), externalData.getErrorSeverity(), ErrorStatus.CREATED,
                externalData.getDateTimeString(),application).build();

//        error.setTitle(externalData.getErrorTitle());
        error.setDescription(externalData.getErrorDescription());
//        error.setStatus(ErrorStatus.CREATED);
//        error.setSeverity(externalData.getErrorSeverity());
//        error.setLastOccurrence(LocalDate.parse(externalData.getDateString(), dateFormatter));
//        error.setDateTime(LocalDateTime.parse(externalData.getDateTimeString(), dateTimeFormatter));
//        error.setApplication(application);
//        error.setCount(1);

        updatePossibleNullValues(error, externalData);

        error.setStackTrace(generateStackTrace(error, externalData.getStackTrace()));

        return errorRepository.save(error);
    }

    private void updatePossibleNullValues(Error error, ExternalData externalData) {
        if (shouldSetField(error.getQueryParams(), externalData.getQueryParams())) {
            error.setQueryParams(externalData.getQueryParams());
        }
        if (shouldSetField(error.getRequestUrl(), externalData.getRequestUrl())) {
            error.setRequestUrl(externalData.getRequestUrl());
        }
        if (shouldSetField(error.getUserAgent(), externalData.getUserAgentString())) {
            UserAgentDetector userAgentDetector = new UserAgentDetector();
            UserAgentData savedUAD = userAgentDataRepository.save(new UserAgentData(userAgentDetector.parseUserAgent(externalData.getUserAgentString())));
            error.setUserAgent(savedUAD);
        }
        if (shouldSetField(error.getRequestParams(), externalData.getRequestParams())) {
            error.setRequestParams(externalData.getRequestParams().stream().collect(Collectors.joining("\n")));
        }
        if (shouldSetField(error.getRequestHeaders(), externalData.getRequestHeaders())) {
            error.setRequestParams(externalData.getRequestHeaders().stream().collect(Collectors.joining("\n")));
        }
        if(shouldSetField(error.getRequestMethod(), externalData.getRequestMethod())) {
            error.setRequestMethod(externalData.getRequestMethod());
        }
    }

    private boolean shouldSetField(String oldValue, String newValue) {
        return ValidationUtil.isBlank(oldValue) && !ValidationUtil.isBlank(newValue);
    }

    private boolean shouldSetField(Object oldValue, Object newValue) {
        return ValidationUtil.isNull(oldValue) && ValidationUtil.isNotNull(newValue);
    }

    private boolean shouldSetField(String oldValue, List<String> newValues) {
        return ValidationUtil.isNull(oldValue) && ValidationUtil.isNotNull(newValues) && !newValues.isEmpty();
    }

    private List<ErrorStackTrace> generateStackTrace(Error error, List<ExternalData.ExternalStackTrace> extStackTraces) {
        List<ErrorStackTrace> stackTraces = new ArrayList<>();

        extStackTraces.forEach(externalStackTrace -> {
            ErrorStackTrace errorStackTrace = new ErrorStackTrace(error, externalStackTrace.getOrder(), externalStackTrace.getDescription());
//            errorStackTrace.setError(error);
//            errorStackTrace.setStackTrace(externalStackTrace.getDescription());
//            errorStackTrace.setStackTraceOrder(externalStackTrace.getOrder());

            stackTraces.add(errorStackTrace);
        });

        return stackTraces;
    }

    private boolean stackTracesAreEqual(List<ExternalData.ExternalStackTrace> extStackTraces, List<ErrorStackTrace> stackTraces) {
        for (int i = 0; i < stackTraces.size(); i++) {
            if (stackTraceNotEqual(extStackTraces.get(i), stackTraces.get(i))) {
                return false;
            }
        }
        return true;
    }

    private boolean stackTraceNotEqual(ExternalData.ExternalStackTrace extStackTrace, ErrorStackTrace stackTrace) {
        return !extStackTrace.getDescription().equals(stackTrace.getStackTrace());
    }

    private Error checkDuplicateOrCreate(ExternalData externalData, Application application, List<Error> foundDuplicates) {
        if (foundDuplicates.size() > 1) {
            logger.warn("More than one duplicate found. Algorithm should be improved.");
        }

        for (Error possibleDuplicate : foundDuplicates) {
            if (checkStackTrace(externalData, possibleDuplicate)) {
//                possibleDuplicate.setLastOccurrence(LocalDate.parse(externalData.getDateString(), dateFormatter));
                possibleDuplicate.parseAndSetLastOccurrence(externalData.getDateString());
                possibleDuplicate.incrementCount();
                if (possibleDuplicate.getStatus().equals(ErrorStatus.RESOLVED)) {
                    possibleDuplicate.setStatus(ErrorStatus.REOPENED);
                }
                updatePossibleNullValues(possibleDuplicate, externalData);

                return errorRepository.save(possibleDuplicate);
            }
        }

        return createNewError(externalData, application);
    }

    private boolean checkStackTrace(ExternalData externalData, Error possibleDuplicate) {

        List<ExternalData.ExternalStackTrace> stackTraces = externalData.getStackTrace().stream()
                .sorted(Comparator.comparingInt(ExternalData.ExternalStackTrace::getOrder))
                .collect(Collectors.toList());

        if (stackTraces.size() != possibleDuplicate.getStackTrace().size()) {
            logger.debug("Stack traces have different length.");
            return false;
        }

        return stackTracesAreEqual(stackTraces, possibleDuplicate.getStackTrace());
    }
}
