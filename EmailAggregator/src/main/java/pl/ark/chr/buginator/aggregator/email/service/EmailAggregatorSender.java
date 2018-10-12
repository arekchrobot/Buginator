package pl.ark.chr.buginator.aggregator.email.service;

import org.springframework.stereotype.Service;
import pl.ark.chr.buginator.aggregator.email.domain.EmailAggregator;
import pl.ark.chr.buginator.aggregator.service.AbstractAggregatorSender;
import pl.ark.chr.buginator.aggregator.service.AggregatorSender;
import pl.ark.chr.buginator.domain.core.Error;

/**
 * Implementation of AggregatorSender
 * Sends error notification via mail to predefined recipients
 */
@Service
public class EmailAggregatorSender extends AbstractAggregatorSender<EmailAggregator> implements AggregatorSender<EmailAggregator> {

    @Override
    public boolean isValid(String aggregatorClass) {
        return !aggregatorClass.isBlank() && aggregatorClass.equals(EmailAggregator.EMAIL_AGGREGATOR_NAME);
    }

    //TODO: implement sedning emails to JMS
    @Override
    protected void sendData(EmailAggregator aggregator, Error error) {
        throw new UnsupportedOperationException("Method not implemented");
    }
}
