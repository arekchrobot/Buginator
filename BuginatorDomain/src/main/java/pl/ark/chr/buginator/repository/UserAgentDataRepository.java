package pl.ark.chr.buginator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.ark.chr.buginator.domain.UserAgentData;

/**
 * Created by Arek on 2016-09-29.
 */
public interface UserAgentDataRepository extends JpaRepository<UserAgentData, Long> {
}
