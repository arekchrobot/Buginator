package pl.ark.chr.buginator.repository.auth;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.ark.chr.buginator.domain.auth.Permission;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
}
