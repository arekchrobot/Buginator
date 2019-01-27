package pl.ark.chr.buginator.auth.util;

import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.ark.chr.buginator.domain.auth.Company;
import pl.ark.chr.buginator.domain.auth.PaymentOption;
import pl.ark.chr.buginator.domain.auth.Role;
import pl.ark.chr.buginator.domain.auth.User;
import pl.ark.chr.buginator.domain.messaging.EmailMessage;

import java.util.HashMap;
import java.util.Map;

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

    public static User createUser(Company company, Role role) {
        return createUser(company, role, true);
    }

    public static User createUser(Company company, Role role, boolean active) {
        return User.builder()
                .name("TEST_USER")
                .email("test@gmail.com")
                .password("{def}$2a$10$ra/Scxal23zJrh.sh8nQP.LreuuTp0Ez8L9/aeQCA4AzRXct6zlea")
                .active(active)
                .company(company)
                .role(role)
                .build();
    }

    @SuppressWarnings("deprecation")
    public static PasswordEncoder passwordEncoder() {
        Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put("def", NoOpPasswordEncoder.getInstance());

        DelegatingPasswordEncoder delegatingPasswordEncoder = new DelegatingPasswordEncoder("def", encoders);
        delegatingPasswordEncoder.setDefaultPasswordEncoderForMatches(encoders.get("def"));

        return delegatingPasswordEncoder;
    }

    public static EmailMessage createEmailMessage() {
        return EmailMessage.builder()
                .from("test.junit@junit.com")
                .username("test.junit@junit.com")
                .password("pass")
                .smtpHost("host.test.com")
                .smtpPort("455")
                .ssl(false)
                .build();
    }
}
