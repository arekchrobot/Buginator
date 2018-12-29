package pl.ark.chr.buginator.service;

import pl.ark.chr.buginator.data.AggregatorData;
import pl.ark.chr.buginator.data.UserWrapper;
import pl.ark.chr.buginator.exceptions.DataAccessException;

import java.util.List;

/**
 * Created by Arek on 2017-03-15.
 */
public interface AggregatorService {

    List<AggregatorData> getAllAggregatorsForApplication(Long applicationId, UserWrapper userWrapper) throws DataAccessException, ClassNotFoundException;

    AggregatorData saveNewAggregator(AggregatorData aggregatorData, UserWrapper userWrapper) throws DataAccessException, ClassNotFoundException;

    AggregatorData getEmptyAggregator(String aggregatorType) throws ClassNotFoundException;

    AggregatorData getAggregator(Long aggregatorId, UserWrapper userWrapper)  throws DataAccessException, ClassNotFoundException;

    AggregatorData updateAggregator(AggregatorData aggregatorData, UserWrapper userWrapper) throws DataAccessException, ClassNotFoundException;

    void removeAggregator(Long aggregatorId, UserWrapper userWrapper) throws DataAccessException, ClassNotFoundException;
}
