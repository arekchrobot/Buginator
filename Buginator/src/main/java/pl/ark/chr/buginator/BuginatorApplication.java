package pl.ark.chr.buginator;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import pl.ark.chr.buginator.websocket.NotificationEndpoint;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
@EnableAsync
@EnableWebSocketMessageBroker
@EnableScheduling
public class BuginatorApplication {

    @Autowired
    private BuginatorProperties buginatorProperties;

    @Bean(destroyMethod = "shutdown")
    public Executor taskScheduler() {
        return Executors.newScheduledThreadPool(buginatorProperties.getSchedulerThreads());
    }

    @Bean
    @Profile("dev")
    public EmbeddedServletContainerFactory servletContainer() {
        TomcatEmbeddedServletContainerFactory tomcat = new TomcatEmbeddedServletContainerFactory() {
            @Override
            protected void postProcessContext(Context context) {
                SecurityConstraint securityConstraint = new SecurityConstraint();
                securityConstraint.setUserConstraint("CONFIDENTIAL");
                SecurityCollection collection = new SecurityCollection();
                collection.addPattern("/*");
                securityConstraint.addCollection(collection);
                context.addConstraint(securityConstraint);
            }
        };

        tomcat.addAdditionalTomcatConnectors(initiateHttpConnector());
        return tomcat;
    }

    private Connector initiateHttpConnector() {
        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        connector.setScheme("http");
        connector.setPort(8080);
        connector.setSecure(false);
        connector.setRedirectPort(8443);

        return connector;
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

//    public static void main(String[] args) {
//
//        ConfigurableApplicationContext run = SpringApplication.run(BuginatorApplication.class, args);
//
//        SimpMessagingTemplate simpMessagingTemplate = run.getBean(SimpMessagingTemplate.class);
//
//        NotificationEndpoint endpoint = new NotificationEndpoint();
//
//        ScheduledExecutorService ses = Executors.newScheduledThreadPool(1);
//        ses.scheduleAtFixedRate(() -> {
//            System.out.println("Broadcasting");
//            try {
//                simpMessagingTemplate.convertAndSend("/topic/notification/User1", endpoint.sendNotification("token is cringy"));
//            } catch (Exception ex) {
//                System.out.println("Error: " + ex.getMessage());
//                ex.printStackTrace();
//            }
//        }, 0, 3, TimeUnit.SECONDS);
//    }
}
