package pl.ark.chr.buginator.repository.util;

import pl.ark.chr.buginator.domain.auth.*;

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

    public static Role createRole() {
        var role = new Role();
        role.setName("TEST_ROLE");
        return role;
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

    public static PasswordReset createPasswordReset(User user, String token) {
        return createPasswordReset(user, token, LocalDateTime.now());
    }

    public static PasswordReset createPasswordReset(User user, String token, LocalDateTime createdAt) {
        return new PasswordReset(user, token, createdAt);
    }
}
