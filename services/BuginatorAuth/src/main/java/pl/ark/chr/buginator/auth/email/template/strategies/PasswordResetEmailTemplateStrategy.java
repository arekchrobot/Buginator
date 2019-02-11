package pl.ark.chr.buginator.auth.email.template.strategies;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import pl.ark.chr.buginator.auth.email.template.EmailTemplateStrategy;
import pl.ark.chr.buginator.auth.email.template.EmailType;
import pl.ark.chr.buginator.commons.util.NetworkUtil;
import pl.ark.chr.buginator.domain.auth.Company;
import pl.ark.chr.buginator.domain.auth.User;
import pl.ark.chr.buginator.repository.auth.PasswordResetRepository;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

@Service
public class PasswordResetEmailTemplateStrategy implements EmailTemplateStrategy {

    private Configuration freeMarkerConfig;
    private MessageSource authEmailMessageSource;
    private PasswordResetRepository passwordResetRepository;

    private static final String TEMPLATE_PATH = "/passwordReset.html";

    @Autowired
    public PasswordResetEmailTemplateStrategy(Configuration freeMarkerConfig, MessageSource authEmailMessageSource,
                                              PasswordResetRepository passwordResetRepository) {
        this.freeMarkerConfig = freeMarkerConfig;
        this.authEmailMessageSource = authEmailMessageSource;
        this.passwordResetRepository = passwordResetRepository;
    }

    @Override
    public boolean isValid(EmailType type) {
        return EmailType.PASSWORD_RESET.equals(type);
    }

    @Override
    public String constructEmailBody(User user, Company company, Locale locale) throws IOException, TemplateException {
        Objects.requireNonNull(user);

        Map<String, Object> model = new HashMap<>();

        model.put("passwordResetTitle", authEmailMessageSource.getMessage("email.password.reset.title", null, locale));
        model.put("passwordResetGreeting", authEmailMessageSource.getMessage("email.password.reset.greeting", null, locale));
        model.put("userName", user.getName());
        model.put("passwordResetInfo", authEmailMessageSource.getMessage("email.password.reset.info", null, locale));

        String url = generateUrl(user);
        model.put("passwordResetUrl", url);

        return constructEmailBody(model);
    }

    private String generateUrl(User user) {
        return "http://"
                + NetworkUtil.getHostIP()
                + ":"
                + NetworkUtil.getHostPort()
                + "/changePassword?token="
                + passwordResetRepository.findFirstByUserOrderByCreatedAtDesc(user).getToken();
    }

    @Override
    public String getSubject(Locale locale) {
        return authEmailMessageSource.getMessage("email.password.reset.subject", null, locale);
    }

    String constructEmailBody(Map model) throws IOException, TemplateException {
        var template = freeMarkerConfig.getTemplate(TEMPLATE_PATH);
        return FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
    }
}
