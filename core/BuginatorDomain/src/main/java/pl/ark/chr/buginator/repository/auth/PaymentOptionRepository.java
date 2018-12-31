package pl.ark.chr.buginator.repository.auth;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.ark.chr.buginator.domain.auth.PaymentOption;

public interface PaymentOptionRepository extends JpaRepository<PaymentOption, Long> {

    PaymentOption findByName(String name);
}
