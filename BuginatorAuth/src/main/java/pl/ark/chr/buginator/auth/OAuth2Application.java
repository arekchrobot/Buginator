package pl.ark.chr.buginator.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan({"pl.ark.chr.buginator.domain", "pl.ark.chr.buginator.persistence"})
@EnableJpaRepositories({"pl.ark.chr.buginator.repository"})
public class OAuth2Application {

    public static void main(String[] args) {
        SpringApplication.run(OAuth2Application.class, args);
    }
}
