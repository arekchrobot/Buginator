package pl.ark.chr.buginator.aggregator.email;

import pl.ark.chr.buginator.domain.auth.Company;
import pl.ark.chr.buginator.domain.auth.PaymentOption;
import pl.ark.chr.buginator.domain.core.Application;
import pl.ark.chr.buginator.domain.core.Error;
import pl.ark.chr.buginator.domain.core.ErrorSeverity;
import pl.ark.chr.buginator.domain.core.ErrorStatus;

public class TestObjectCreator {

    static EmailAggregator.Builder getPrecofiguredEmailAggregatorBuilder(Application app) {
        return new EmailAggregator.Builder(app, "tester@gmail.com")
                .cc("ccTester@gmail.com")
                .login("loginName")
                .password("password")
                .smtpHost("smtp.gmail.com")
                .smtpPort("578");
    }

    static Application createTestApplication() {
        var company = new Company("company", new PaymentOption());
        var app = new Application("app", company);
        app.setId(1L);
        return app;
    }

    static Error createTestError(Application app) {
        var error = Error.builder("error", ErrorSeverity.ERROR, ErrorStatus.CREATED,
                "2018-11-11 11:11:11", app)
                .build();
        error.setId(2L);

        return error;
    }
}
