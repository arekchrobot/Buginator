package pl.ark.chr.buginator.security.actuator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ActuatorSecurityConfig {

    private final String actuatorUsername;
    private final String actuatorPass;

    public ActuatorSecurityConfig(@Value("${buginator.actuator.credentials.username}") String actuatorUsername,
                                  @Value("${buginator.actuator.credentials.password}") String actuatorPass) {
        this.actuatorUsername = actuatorUsername;
        this.actuatorPass = actuatorPass;
    }

    @Bean
    public FilterRegistrationBean actuatorSecurityFilter() {
        FilterRegistrationBean bean =
                new FilterRegistrationBean<>(new ActuatorSecurityFilter(actuatorUsername, actuatorPass));
        bean.setOrder(1);
        return bean;
    }
}
