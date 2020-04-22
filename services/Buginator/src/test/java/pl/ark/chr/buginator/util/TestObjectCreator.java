package pl.ark.chr.buginator.util;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import pl.ark.chr.buginator.app.application.UserApplicationDTO;
import pl.ark.chr.buginator.commons.dto.LoggedUserDTO;
import pl.ark.chr.buginator.commons.util.Pair;
import pl.ark.chr.buginator.domain.auth.*;
import pl.ark.chr.buginator.domain.core.Application;
import pl.ark.chr.buginator.domain.core.Error;
import pl.ark.chr.buginator.domain.core.ErrorSeverity;
import pl.ark.chr.buginator.domain.core.ErrorStatus;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestObjectCreator {

    public static PaymentOption createPaymentOption(String name) {
        var paymentOption = new PaymentOption();
        paymentOption.setDuration(30);
        paymentOption.setMaxUsers(5);
        paymentOption.setPrice(0.0);
        paymentOption.setName(name);
        return paymentOption;
    }

    public static Company createCompany(PaymentOption paymentOption) {
        return createCompany("TEST", paymentOption);
    }

    public static Company createCompany(String name, PaymentOption paymentOption) {
        return new Company(name, paymentOption);
    }

    public static User createUser(Company company, Role role, String email) {
        return createUser(company, role, true, email);
    }

    public static User createUser(Company company, Role role, boolean active, String email) {
        return User.builder()
                .name(email)
                .email(email)
                .password("{def}$2a$10$ra/Scxal23zJrh.sh8nQP.LreuuTp0Ez8L9/aeQCA4AzRXct6zlea")
                .active(active)
                .company(company)
                .role(role)
                .build();
    }

    public static void setAuthentication(AuthenticationManager authenticationManager, String email) {
        var usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(email, "123");
        var auth = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    public static Application createApplication(String name, Company company) {
        return new Application(name, company);
    }

    public static Application createApplicationWithId(String name, Company company, Long id) {
        var app =  new Application(name, company);
        app.setId(id);
        return app;
    }

    public static Pair<Application, UserApplication> createApplicationForUser(String name, Company company, User user) {
        var application = createApplication(name, company);
        var userApplication = new UserApplication(user, application);
        userApplication.setModify(true);
        return new Pair<>(application, userApplication);
    }

    public static Error createError(Application application, String title, LocalDateTime occurrence) {
        return createErrorBuilder(title, ErrorSeverity.ERROR, ErrorStatus.CREATED, occurrence, application)
                .build();
    }

    public static LoggedUserDTO loggedUser(String email, Long companyId) {
        return new LoggedUserDTO(email, email, Set.of("MANAGER"), companyId);
    }

    public static Error.Builder createErrorBuilder(String title, ErrorSeverity severity, ErrorStatus status,
                                                   LocalDateTime occurrence, Application application) {
        return Error.builder(title, severity, status, occurrence.format(Error.DATE_TIME_FORMATTER), application);
    }

    public static Set<UserApplicationDTO> createUserApps(String baseAppName, int count) {
        UserApplicationDTO userApp1 = UserApplicationDTO.builder()
                .id(1L)
                .name(baseAppName + 1)
                .modify(true)
                .build();

        return Stream
                .iterate(userApp1, ua ->
                        UserApplicationDTO.builder()
                                .id(ua.getId() + 1)
                                .name(baseAppName + (ua.getId() + 1))
                                .modify(ua.getId() % 2 == 0)
                                .build()
                )
                .limit(count)
                .collect(Collectors.toSet());
    }

    public static PaymentOption trialPaymentOption() {
        var paymentOption = TestObjectCreator.createPaymentOption("Trial");
        paymentOption.setId(PaymentOption.TRIAL);
        return paymentOption;
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

        Error error2 = Error.builder("t1", ErrorSeverity.ERROR, ErrorStatus.CREATED, now.format(Error.DATE_TIME_FORMATTER),
                application)
                .lastOccurrence(minusSevenDays.format(Error.DATE_TIME_FORMATTER))
                .build();

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
}
