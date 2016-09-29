package pl.ark.chr.buginator.domain.aggregator;

import pl.ark.chr.buginator.domain.Aggregator;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by Arek on 2016-09-28.
 */
@Entity
@Table(name = "jira_aggregator")
public class JiraAggregator extends Aggregator {
}
