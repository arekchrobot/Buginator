package pl.ark.chr.buginator.repository.auth;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import pl.ark.chr.buginator.domain.auth.Company;
import pl.ark.chr.buginator.domain.auth.Role;

import java.util.List;

public interface RoleRepository extends CrudRepository<Role, Long> {

    Role findByName(String name);

    @Query("select r from Role r where r.company = ?1 or r.company is null")
    List<Role> findAllByCompany(Company company);
}
