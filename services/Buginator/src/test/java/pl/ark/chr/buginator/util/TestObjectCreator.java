package pl.ark.chr.buginator.util;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import pl.ark.chr.buginator.commons.util.Pair;
import pl.ark.chr.buginator.domain.auth.*;
import pl.ark.chr.buginator.domain.core.Application;
import pl.ark.chr.buginator.domain.core.Error;
import pl.ark.chr.buginator.domain.core.ErrorSeverity;
import pl.ark.chr.buginator.domain.core.ErrorStatus;

import java.time.LocalDateTime;

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
        return new Company("TEST", paymentOption);
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

    public static Pair<Application, UserApplication> createApplicationForUser(String name, Company company, User user) {
        var application = createApplication(name, company);
        var userApplication = new UserApplication(user, application);
        userApplication.setModify(true);
        return new Pair<>(application, userApplication);
    }

    public static Error createError(Application application, String title, LocalDateTime occurence) {
        return Error.builder(title, ErrorSeverity.ERROR, ErrorStatus.CREATED,
                occurence.format(Error.DATE_TIME_FORMATTER), application)
                .build();
    }
}
