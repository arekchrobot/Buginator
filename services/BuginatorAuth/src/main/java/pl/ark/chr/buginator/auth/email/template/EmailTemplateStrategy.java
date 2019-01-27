package pl.ark.chr.buginator.auth.email.template;

import freemarker.template.TemplateException;
import pl.ark.chr.buginator.domain.auth.Company;

import java.io.IOException;
import java.util.Locale;

public interface EmailTemplateStrategy {

    boolean isValid(EmailType type);

    String constructEmailBody(Company company, Locale locale) throws IOException, TemplateException;

    String getSubject(Locale locale);
}
