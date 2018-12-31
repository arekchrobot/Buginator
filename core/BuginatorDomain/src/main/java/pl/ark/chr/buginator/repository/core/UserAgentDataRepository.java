package pl.ark.chr.buginator.repository.core;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.ark.chr.buginator.domain.core.UserAgentData;

//TODO: check possibility to be removed and managed by Error (using cascade from JPA)
public interface UserAgentDataRepository extends JpaRepository<UserAgentData, Long> {
}
