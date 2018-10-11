package pl.ark.chr.buginator.service;

import pl.ark.chr.buginator.domain.aggregator.AggregatorLog;
import pl.ark.chr.buginator.domain.auth.UserApplication;
import pl.ark.chr.buginator.exceptions.DataAccessException;

import java.util.List;
import java.util.Set;

/**
 * Created by Arek on 2017-04-06.
 */
public interface AggregatorLogService {

    List<AggregatorLog> getAllByAggregator(Long aggregatorId, Set<UserApplication> userApplications)  throws DataAccessException;
}
