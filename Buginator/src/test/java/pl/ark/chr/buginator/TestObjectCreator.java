package pl.ark.chr.buginator;

import pl.ark.chr.buginator.data.UserWrapper;
import pl.ark.chr.buginator.domain.auth.*;
import pl.ark.chr.buginator.domain.core.Application;
import pl.ark.chr.buginator.domain.core.Error;
import pl.ark.chr.buginator.domain.core.ErrorStackTrace;
import pl.ark.chr.buginator.domain.core.UserAgentData;
import pl.ark.chr.buginator.domain.core.ErrorSeverity;
import pl.ark.chr.buginator.domain.core.ErrorStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Arek on 2016-12-26.
 */
public class TestObjectCreator {

    public static Role createRole(String name) {
        Role role = new Role();
        role.setId(1L);
        if (name != null) {
            role.setName(name);
        } else {
            role.setName("Test role");
        }

        return role;
    }

    public static User createUser(String email, Role role, Company company, String password) {
        User user = new User.Builder().build();
        user.setEmail(email);
        user.setId(1L);
        user.setRole(role);
        user.setCompany(company);
        if(password != null) {
            user.setPassword(password);
        }

        return user;
    }

    public static Company createCompany(LocalDate expiryDate, String name, String token, String uniqueId) {
        Company company = new Company(name, new PaymentOption());
        company.setId(1L);
//        company.setExpiryDate(expiryDate);
//        if(name != null) {
//            company.setName(name);
//        }
//        if(token != null) {
//            company.setToken(token);
//        }
//        if(uniqueId != null) {
//            company.setUniqueKey(uniqueId);
//        }

        return company;
    }

    public static Application createApplication(Company company, String name) {
        return createApplication(company, name, 1L);
    }

    public static Application createApplication(Company company, String name, long id) {
        Application application = new Application(name != null ? name : "Test Application", company);
//        application.setName(name != null ? name : "Test Application");
        application.setId(id);
//        application.setCompany(company);

        return application;
    }

    public static UserApplication createUserApplication(User user, Application application, boolean modify) {
        UserApplication userApplication = new UserApplication(user, application);
//        userApplication.setApplication(application);
//        userApplication.setUser(user);
        userApplication.setModify(modify);

        return userApplication;
    }

    public static UserWrapper createUserWrapper(User user) {
        return new UserWrapper(user, "123:abc");
    }

    public static List<Error> generateErrorListForLastWeekForApplication(Application application) {
        LocalDateTime now = LocalDateTime.now();

        LocalDateTime minusSevenDays = now.minusDays(7);
        LocalDateTime minusFourDays = now.minusDays(4);
        LocalDateTime minusOneDays = now.minusDays(1);

        Error error1 = Error.builder("t1", ErrorSeverity.ERROR, ErrorStatus.CREATED, now.format(Error.DATE_TIME_FORMATTER),
                application)
                .lastOccurrence(minusSevenDays.format(Error.DATE_TIME_FORMATTER))
                .build();
//        error1.setApplication(application);
//        error1.setLastOccurrence(minusSevenDays);

        Error error2 = Error.builder("t1", ErrorSeverity.ERROR, ErrorStatus.CREATED, now.format(Error.DATE_TIME_FORMATTER),
                application)
                .lastOccurrence(minusSevenDays.format(Error.DATE_TIME_FORMATTER))
                .build();
//        error2.setApplication(application);
//        error2.setLastOccurrence(minusSevenDays);

        Error error3 = Error.builder("t1", ErrorSeverity.ERROR, ErrorStatus.CREATED, now.format(Error.DATE_TIME_FORMATTER),
                application)
                .lastOccurrence(minusSevenDays.format(Error.DATE_TIME_FORMATTER))
                .build();

        Error error4= Error.builder("t1", ErrorSeverity.ERROR, ErrorStatus.CREATED, now.format(Error.DATE_TIME_FORMATTER),
                application)
                .lastOccurrence(minusFourDays.format(Error.DATE_TIME_FORMATTER))
                .build();

        Error error5 = Error.builder("t1", ErrorSeverity.ERROR, ErrorStatus.CREATED, now.format(Error.DATE_TIME_FORMATTER),
                application)
                .lastOccurrence(minusFourDays.format(Error.DATE_TIME_FORMATTER))
                .build();

        Error error6 = Error.builder("t1", ErrorSeverity.ERROR, ErrorStatus.CREATED, now.format(Error.DATE_TIME_FORMATTER),
                application)
                .lastOccurrence(minusOneDays.format(Error.DATE_TIME_FORMATTER))
                .build();

        Error error7 = Error.builder("t1", ErrorSeverity.ERROR, ErrorStatus.CREATED, now.format(Error.DATE_TIME_FORMATTER),
                application)
                .lastOccurrence(now.format(Error.DATE_TIME_FORMATTER))
                .build();

        Error error8 = Error.builder("t1", ErrorSeverity.ERROR, ErrorStatus.CREATED, now.format(Error.DATE_TIME_FORMATTER),
                application)
                .lastOccurrence(now.format(Error.DATE_TIME_FORMATTER))
                .build();

        Error error9 = Error.builder("t1", ErrorSeverity.ERROR, ErrorStatus.CREATED, now.format(Error.DATE_TIME_FORMATTER),
                application)
                .lastOccurrence(now.format(Error.DATE_TIME_FORMATTER))
                .build();

        return Arrays.asList(error1,error2,error3,error4,error5,error6,error7,error8,error9);
    }

    public static List<Error> generateErrorListForSorting(Application application) {
        LocalDateTime now = LocalDateTime.now();

        LocalDateTime minusSevenDays = now.minusDays(7);
        LocalDateTime minusFourDays = now.minusDays(4);
        LocalDateTime minusOneDays = now.minusDays(1);

        Error error1 = Error.builder("t1", ErrorSeverity.CRITICAL, ErrorStatus.ONGOING, now.format(Error.DATE_TIME_FORMATTER),
                application)
                .lastOccurrence(minusSevenDays.format(Error.DATE_TIME_FORMATTER))
                .build();
        error1.setId(1L);
//        error1.setApplication(application);
//        error1.setLastOccurrence(minusSevenDays);
//        error1.setSeverity(ErrorSeverity.CRITICAL);
//        error1.setStatus(ErrorStatus.ONGOING);

        Error error2 = Error.builder("t1", ErrorSeverity.CRITICAL, ErrorStatus.CREATED, now.format(Error.DATE_TIME_FORMATTER),
                application)
                .lastOccurrence(minusSevenDays.format(Error.DATE_TIME_FORMATTER))
                .build();
        error2.setId(2L);
//        error2.setApplication(application);
//        error2.setLastOccurrence(minusSevenDays);
//        error2.setSeverity(ErrorSeverity.CRITICAL);
//        error2.setStatus(ErrorStatus.CREATED);

        Error error3 = Error.builder("t1", ErrorSeverity.ERROR, ErrorStatus.RESOLVED, now.format(Error.DATE_TIME_FORMATTER),
                application)
                .lastOccurrence(minusFourDays.format(Error.DATE_TIME_FORMATTER))
                .build();
        error3.setId(3L);
//        error3.setApplication(application);
//        error3.setLastOccurrence(minusFourDays);
//        error3.setSeverity(ErrorSeverity.ERROR);
//        error3.setStatus(ErrorStatus.RESOLVED);

        Error error4 = Error.builder("t1", ErrorSeverity.ERROR, ErrorStatus.CREATED, now.format(Error.DATE_TIME_FORMATTER),
                application)
                .lastOccurrence(minusFourDays.format(Error.DATE_TIME_FORMATTER))
                .build();
        error4.setId(4L);
//        error4.setApplication(application);
//        error4.setLastOccurrence(minusFourDays);
//        error4.setSeverity(ErrorSeverity.ERROR);
//        error4.setStatus(ErrorStatus.CREATED);

        Error error5 = Error.builder("t1", ErrorSeverity.ERROR, ErrorStatus.ONGOING, now.format(Error.DATE_TIME_FORMATTER),
                application)
                .lastOccurrence(minusOneDays.format(Error.DATE_TIME_FORMATTER))
                .build();
        error5.setId(5L);
//        error5.setApplication(application);
//        error5.setLastOccurrence(minusOneDays);
//        error5.setSeverity(ErrorSeverity.ERROR);
//        error5.setStatus(ErrorStatus.ONGOING);

        Error error6 = Error.builder("t1", ErrorSeverity.CRITICAL, ErrorStatus.ONGOING, now.format(Error.DATE_TIME_FORMATTER),
                application)
                .lastOccurrence(minusOneDays.format(Error.DATE_TIME_FORMATTER))
                .build();
        error6.setId(6L);
//        error6.setApplication(application);
//        error6.setLastOccurrence(minusOneDays);
//        error6.setSeverity(ErrorSeverity.CRITICAL);
//        error6.setStatus(ErrorStatus.ONGOING);

        Error error7 = Error.builder("t1", ErrorSeverity.WARNING, ErrorStatus.ONGOING, now.format(Error.DATE_TIME_FORMATTER),
                application)
                .lastOccurrence(minusOneDays.format(Error.DATE_TIME_FORMATTER))
                .build();
        error7.setId(7L);
//        error7.setApplication(application);
//        error7.setLastOccurrence(minusOneDays);
//        error7.setSeverity(ErrorSeverity.WARNING);
//        error7.setStatus(ErrorStatus.ONGOING);

        return Arrays.asList(error1,error2,error3,error4,error5,error6,error7);
    }

    public static Long[] getSortedErrorIdArray() {
        return new Long[]{6L,5L,7L,4L,3L,2L,1L};
    }

    public static Error createFullDataError(boolean sameData, boolean sameStackTrace, Application application) {
        Error error; //= new Error();
        if(sameData) {
            error = Error.builder("Test Title 1", ErrorSeverity.ERROR, ErrorStatus.CREATED, LocalDateTime.now().format(Error.DATE_TIME_FORMATTER),
                    application)
                    .lastOccurrence(LocalDateTime.now().format(Error.DATE_TIME_FORMATTER))
                    .description("Test description 1")
                    .build();
//            error.setApplication(application);
//            error.setLastOccurrence(LocalDate.now());
//            error.setCount(1);
//            error.setDateTime(LocalDateTime.now());
//            error.setDescription("Test description 1");
//            error.setTitle("Test Title 1");
//            error.setSeverity(ErrorSeverity.ERROR);
        } else {
            error = Error.builder("Test Title 2", ErrorSeverity.WARNING, ErrorStatus.CREATED, LocalDateTime.now().minusDays(1).format(Error.DATE_TIME_FORMATTER),
                    application)
                    .lastOccurrence(LocalDateTime.now().format(Error.DATE_TIME_FORMATTER))
                    .description("Test description 2")
                    .build();
//            error.setApplication(application);
//            error.setLastOccurrence(LocalDate.now());
//            error.setCount(1);
//            error.setDateTime(LocalDateTime.now().minusDays(1));
//            error.setDescription("Test description 2");
//            error.setTitle("Test Title 2");
//            error.setSeverity(ErrorSeverity.WARNING);
        }

        if(sameStackTrace) {
            List<ErrorStackTrace> errorStackTraces = new ArrayList<>();

            ErrorStackTrace errorStackTrace = new ErrorStackTrace(error, 1, "Test stack trace 1");
//            errorStackTrace.setStackTraceOrder(1);
//            errorStackTrace.setStackTrace("Test stack trace 1");
//            errorStackTrace.setError(error);

            ErrorStackTrace errorStackTrace2 = new ErrorStackTrace(error, 2,"Test stack trace 2");
//            errorStackTrace2.setStackTraceOrder(2);
//            errorStackTrace2.setStackTrace("Test stack trace 2");
//            errorStackTrace2.setError(error);

            errorStackTraces.add(errorStackTrace);
            errorStackTraces.add(errorStackTrace2);

            error.setStackTrace(errorStackTraces);
        } else {
            List<ErrorStackTrace> errorStackTraces = new ArrayList<>();

            ErrorStackTrace errorStackTrace = new ErrorStackTrace(error, 1, "Test stack trace 2");
//            errorStackTrace.setStackTraceOrder(1);
//            errorStackTrace.setStackTrace("Test stack trace 2");
//            errorStackTrace.setError(error);

            ErrorStackTrace errorStackTrace2 = new ErrorStackTrace(error, 2, "Test stack trace 2");
//            errorStackTrace2.setStackTraceOrder(2);
//            errorStackTrace2.setStackTrace("Test stack trace 1");
//            errorStackTrace2.setError(error);

            errorStackTraces.add(errorStackTrace);
            errorStackTraces.add(errorStackTrace2);

            error.setStackTrace(errorStackTraces);
        }

        return error;
    }

    public static UserAgentData createUserAgentData(String device) {
        UserAgentData userAgentData = new UserAgentData(null);

        userAgentData.setDevice(device);

        return userAgentData;
    }
}
