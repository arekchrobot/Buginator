package pl.ark.chr.buginator;

//import org.apache.velocity.app.VelocityEngine;
import org.mockito.Mockito;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.ui.velocity.VelocityEngineFactoryBean;
import pl.ark.chr.buginator.aggregator.service.AggregatorServiceValidator;
import pl.ark.chr.buginator.aggregator.service.impl.EmailAggregatorServiceImpl;
import pl.ark.chr.buginator.domain.EmailAggregator;
import pl.ark.chr.buginator.repository.aggregator.AggregatorRepository;
import pl.ark.chr.buginator.repository.EmailAggregatorRepository;
import pl.ark.chr.buginator.service.EmailService;
import pl.ark.chr.buginator.service.impl.EmailServiceImpl;

import java.util.Properties;

/**
 * Created by Arek on 2016-11-27.
 */
@Profile("UNIT_TEST")
@Configuration
public class TestApplicationConfiguration {

    @Bean
    @Primary
    public static PropertySourcesPlaceholderConfigurer properties() throws Exception {
        final PropertySourcesPlaceholderConfigurer pspc = new PropertySourcesPlaceholderConfigurer();
        Properties properties = new Properties();

        properties.setProperty("spring.mail.username", "buginator.noreply@gmail.com");

        pspc.setProperties(properties);
        return pspc;
    }

    //TODO: add another engine template
//    @Bean
//    @Primary
//    public VelocityEngine velocityEngine() throws IOException {
//        VelocityEngineFactoryBean fb = new VelocityEngineFactoryBean();
//        fb.setResourceLoaderPath("classpath:/velocity/");
//
//        Properties props = new Properties();
//        props.setProperty("resource.loader", "file");
//        props.setProperty("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.FileResourceLoader");
//        props.setProperty("class.resource.loader.path", "classpath:/velocity");
//
//        fb.setVelocityProperties(props);
//        return fb.createVelocityEngine();
//    }

    @Bean
    @Primary
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();

        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setBasenames("classpath:/i18n/email",
                "classpath:/i18n/errors");
        messageSource.setUseCodeAsDefaultMessage(true);

        return messageSource;
    }

    @Bean
    public JavaMailSender javaMailSender() {
        return Mockito.mock(JavaMailSender.class);
    }

    @Bean
    public EmailService emailService() {
        return new EmailServiceImpl();
    }

    @Bean
    public EmailAggregator emailAggregator() {
        return new EmailAggregator();
    }

    @Bean
    public AggregatorServiceValidator aggregatorServiceValidator() {
        return new AggregatorServiceValidator();
    }

    @Bean
    public EmailAggregatorServiceImpl emailAggregatorService() {
        return new EmailAggregatorServiceImpl();
    }

    @Bean
    public EmailAggregatorRepository emailAggregatorRepository() {
        return Mockito.mock(EmailAggregatorRepository.class);
    }

    @Bean
    public AggregatorRepository aggregatorRepository() {
        return Mockito.mock(AggregatorRepository.class);
    }
}
