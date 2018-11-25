package pl.ark.chr.buginator.repository.auth;

import org.springframework.data.repository.CrudRepository;
import pl.ark.chr.buginator.domain.auth.Permission;

public interface PermissionRepository extends CrudRepository<Permission, Long> {
}
