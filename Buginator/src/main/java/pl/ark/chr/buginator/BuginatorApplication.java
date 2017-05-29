package pl.ark.chr.buginator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;

import java.util.concurrent.*;

@SpringBootApplication
@EnableAsync
@EnableWebSocketMessageBroker
@EnableScheduling
@EnableCaching
public class BuginatorApplication {

    @Autowired
    private BuginatorProperties buginatorProperties;

    @Bean(destroyMethod = "shutdown")
    public Executor taskScheduler() {
        return Executors.newScheduledThreadPool(buginatorProperties.getSchedulerThreads());
    }

    @Bean(destroyMethod = "shutdown")
    public ExecutorService innerJobScheduler() {
        return Executors.newFixedThreadPool(buginatorProperties.getInnerJobExecutorThreads());
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();

        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setBasenames("classpath:/i18n/email",
                "classpath:/i18n/errors",
                "classpath:/i18n/chart");
        messageSource.setUseCodeAsDefaultMessage(true);

        return messageSource;
    }

    public static void main(String[] args) {
        SpringApplication.run(BuginatorApplication.class, args);
    }
}
