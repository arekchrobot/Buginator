package pl.ark.chr.buginator.data;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import pl.ark.chr.buginator.domain.aggregator.Aggregator;
import pl.ark.chr.buginator.domain.EmailAggregator;

/**
 * Created by Arek on 2017-03-14.
 */
public class AggregatorData {

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
    @JsonSubTypes({
            @JsonSubTypes.Type(value = Aggregator.class, name = "Base"),
            @JsonSubTypes.Type(value = EmailAggregator.class, name = "Email")
    })
    private Aggregator aggregator;

    public AggregatorData() {
    }

    public AggregatorData(Aggregator aggregator) {
        this.aggregator = aggregator;
    }

    public Aggregator getAggregator() {
        return aggregator;
    }

    public void setAggregator(Aggregator aggregator) {
        this.aggregator = aggregator;
    }
}
