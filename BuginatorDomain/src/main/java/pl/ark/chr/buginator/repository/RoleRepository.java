package pl.ark.chr.buginator.repository;

import org.springframework.data.repository.CrudRepository;
import pl.ark.chr.buginator.domain.Role;

/**
 * Created by Arek on 2016-09-29.
 */
public interface RoleRepository extends CrudRepository<Role, Long> {

    Role findByName(String name);
}
