package pl.ark.chr.buginator.repository.core;

import org.springframework.data.repository.CrudRepository;
import pl.ark.chr.buginator.domain.core.Application;
import pl.ark.chr.buginator.domain.auth.Company;

import java.util.Optional;

public interface ApplicationRepository extends CrudRepository<Application, Long> {

    Optional<Application> findByNameAndCompany(String name, Company company);
}
