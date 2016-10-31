package pl.ark.chr.buginator.repository;

import org.springframework.data.repository.CrudRepository;
import pl.ark.chr.buginator.domain.Company;

import java.util.Optional;

/**
 * Created by Arek on 2016-09-29.
 */
public interface CompanyRepository extends CrudRepository<Company, Long> {

    Optional<Company> findByName(String name);
}
