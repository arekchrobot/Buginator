package pl.ark.chr.buginator.service;

import org.springframework.stereotype.Service;
import pl.ark.chr.buginator.aggregator.service.AbstractAggregatorSender;
import pl.ark.chr.buginator.aggregator.service.AggregatorSender;
import pl.ark.chr.buginator.domain.EmailAggregator;
import pl.ark.chr.buginator.domain.Error;

@Service
public class EmailAggregatorSender extends AbstractAggregatorSender<EmailAggregator> implements AggregatorSender<EmailAggregator> {

    private static final String EMAIL_STRATEGY_NAME = "EmailAggregator";

    @Override
    public boolean isValid(String aggregatorClass) {
        return !aggregatorClass.isBlank() && aggregatorClass.equals(EMAIL_STRATEGY_NAME);
    }

    @Override
    protected void sendData(EmailAggregator aggregator, Error error) {
        throw new UnsupportedOperationException("Method not implemented");
    }
}
