package pl.ark.chr.buginator.security.actuator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.boot.actuate.health.Status;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ActuatorConfig {

    private final String actuatorUsername;
    private final String actuatorPass;
    private final HealthEndpoint healthEndpoint;

    public ActuatorConfig(@Value("${buginator.actuator.credentials.username}") String actuatorUsername,
                          @Value("${buginator.actuator.credentials.password}") String actuatorPass,
                          HealthEndpoint healthEndpoint) {
        this.actuatorUsername = actuatorUsername;
        this.actuatorPass = actuatorPass;
        this.healthEndpoint = healthEndpoint;
    }

    @Bean
    public FilterRegistrationBean actuatorSecurityFilter() {
        FilterRegistrationBean bean =
                new FilterRegistrationBean<>(new ActuatorSecurityFilter(actuatorUsername, actuatorPass));
        bean.setOrder(1);
        return bean;
    }

    @Bean
    public MeterRegistryCustomizer prometheusHealthCheck() {
        return registry -> registry.gauge("health", healthEndpoint, ActuatorConfig::healthToCode);
    }

    private static int healthToCode(HealthEndpoint ep) {
        Status status = ep.health().getStatus();

        return status.equals(Status.UP) ? 1 : 0;
    }
}
