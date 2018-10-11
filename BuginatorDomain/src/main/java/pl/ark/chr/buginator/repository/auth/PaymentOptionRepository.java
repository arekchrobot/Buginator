package pl.ark.chr.buginator.repository.auth;

import org.springframework.data.repository.CrudRepository;
import pl.ark.chr.buginator.domain.auth.PaymentOption;

public interface PaymentOptionRepository extends CrudRepository<PaymentOption, Long> {

    PaymentOption findByName(String name);
}
