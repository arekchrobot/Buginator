package pl.ark.chr.buginator.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.ark.chr.buginator.domain.Aggregator;
import pl.ark.chr.buginator.domain.AggregatorLog;
import pl.ark.chr.buginator.domain.UserApplication;
import pl.ark.chr.buginator.exceptions.DataAccessException;
import pl.ark.chr.buginator.filter.ClientFilter;
import pl.ark.chr.buginator.filter.ClientFilterFactory;
import pl.ark.chr.buginator.repository.AggregatorLogRepository;
import pl.ark.chr.buginator.repository.AggregatorRepository;
import pl.ark.chr.buginator.service.AggregatorLogService;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Arek on 2017-04-06.
 */
@Service
@Transactional
public class AggregatorLogServiceImpl implements AggregatorLogService {

    private static final Logger logger = LoggerFactory.getLogger(AggregatorLogServiceImpl.class);

    private final ClientFilter clientFilter = ClientFilterFactory.createClientFilter(ClientFilterFactory.ClientFilterType.APPLICATION_ACCESS);

    @Autowired
    private AggregatorRepository aggregatorRepository;

    @Autowired
    private AggregatorLogRepository aggregatorLogRepository;

    @Override
    public List<AggregatorLog> getAllByAggregator(Long aggregatorId, Set<UserApplication> userApplications) throws DataAccessException {
        Aggregator aggregator = aggregatorRepository.findOne(aggregatorId);

        if(aggregator == null) {
            logger.error("No aggregator found for id: " + aggregatorId);
            return new ArrayList<>();
        }

        clientFilter.validateAccess(aggregator, userApplications);

        return aggregatorLogRepository.findByAggregator(aggregator);
    }
}