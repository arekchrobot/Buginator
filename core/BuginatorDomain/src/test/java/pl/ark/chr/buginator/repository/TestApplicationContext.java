package pl.ark.chr.buginator.repository;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan({"pl.ark.chr.buginator.domain", "pl.ark.chr.buginator.persistence"})
@EnableJpaRepositories({"pl.ark.chr.buginator.repository"})
public class TestApplicationContext {
}
