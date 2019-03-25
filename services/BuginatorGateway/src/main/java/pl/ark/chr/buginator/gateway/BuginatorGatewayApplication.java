package pl.ark.chr.buginator.gateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.boot.actuate.health.Status;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BuginatorGatewayApplication {

    @Autowired
    private HealthEndpoint healthEndpoint;

    public static void main(String[] args) {
        SpringApplication.run(BuginatorGatewayApplication.class, args);
    }

    @Bean
    public MeterRegistryCustomizer prometheusHealthCheck() {
        return registry -> registry.gauge("health", healthEndpoint, BuginatorGatewayApplication::healthToCode);
    }

    private static int healthToCode(HealthEndpoint ep) {
        Status status = ep.health().getStatus();

        return status.equals(Status.UP) ? 1 : 0;
    }
}
