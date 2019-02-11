package pl.ark.chr.buginator.auth.email.template.strategies;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import pl.ark.chr.buginator.auth.email.template.EmailTemplateStrategy;
import pl.ark.chr.buginator.auth.email.template.EmailType;
import pl.ark.chr.buginator.domain.auth.Company;
import pl.ark.chr.buginator.domain.auth.User;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

@Service
public class RegisterEmailTemplateStrategy implements EmailTemplateStrategy {

    private Configuration freeMarkerConfig;
    private MessageSource authEmailMessageSource;

    private static final String TEMPLATE_PATH = "/registerConfirm.html";

    @Autowired
    public RegisterEmailTemplateStrategy(Configuration freeMarkerConfig, MessageSource authEmailMessageSource) {
        this.freeMarkerConfig = freeMarkerConfig;
        this.authEmailMessageSource = authEmailMessageSource;
    }

    @Override
    public boolean isValid(EmailType type) {
        return EmailType.REGISTER.equals(type);
    }

    @Override
    public String constructEmailBody(User user, Company company, Locale locale) throws IOException, TemplateException {
        Objects.requireNonNull(company);

        Map<String, Object> model = new HashMap<>();

        model.put("registerTitle", authEmailMessageSource.getMessage("email.register.title", null, locale));
        model.put("registerGreeting", authEmailMessageSource.getMessage("email.register.greeting", null, locale));
        model.put("companyName", company.getName());
        model.put("registerInfo", authEmailMessageSource.getMessage("email.register.info", null, locale));
        model.put("uniqueKeyInfo", authEmailMessageSource.getMessage("email.register.uniqueKey", null, locale));
        model.put("uniqueKey", company.getUniqueKey());
        model.put("tokenInfo", authEmailMessageSource.getMessage("email.register.token", null, locale));
        model.put("token", company.getToken());

        return constructEmailBody(model);
    }

    @Override
    public String getSubject(Locale locale) {
        return authEmailMessageSource.getMessage("email.register.subject", null, locale);
    }

    String constructEmailBody(Map model) throws IOException, TemplateException {
        var template = freeMarkerConfig.getTemplate(TEMPLATE_PATH);
        return FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
    }
}
