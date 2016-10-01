package pl.ark.chr.buginator.repository;

import org.springframework.data.repository.CrudRepository;
import pl.ark.chr.buginator.domain.PaymentOption;

/**
 * Created by Arek on 2016-09-29.
 */
public interface PaymentOptionRepository extends CrudRepository<PaymentOption, Long> {
}
