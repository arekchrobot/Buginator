package pl.ark.chr.buginator.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.ark.chr.buginator.aggregator.util.AggregatorReflection;
import pl.ark.chr.buginator.data.AggregatorData;
import pl.ark.chr.buginator.data.UserWrapper;
import pl.ark.chr.buginator.domain.aggregator.Aggregator;
import pl.ark.chr.buginator.domain.core.Application;
import pl.ark.chr.buginator.exceptions.DataAccessException;
import pl.ark.chr.buginator.filter.ClientFilter;
import pl.ark.chr.buginator.filter.ClientFilterFactory;
import pl.ark.chr.buginator.repository.aggregator.AggregatorRepository;
import pl.ark.chr.buginator.repository.core.ApplicationRepository;
import pl.ark.chr.buginator.service.AggregatorService;

import java.util.ArrayList;
import java.util.List;

//import static org.apache.velocity.util.StringUtils.capitalizeFirstLetter;

/**
 * Created by Arek on 2017-03-15.
 */
@Service
@Transactional
public class AggregatorServiceImpl implements AggregatorService {

    private final ClientFilter clientFilter = ClientFilterFactory.createClientFilter(ClientFilterFactory.ClientFilterType.APPLICATION_ACCESS,
            ClientFilterFactory.ClientFilterType.DATA_MODIFY);

    private static final String AGGREGATOR_SUFFIX = "Aggregator";

    @Autowired
    private AggregatorReflection aggregatorReflection;

    @Autowired
    private ApplicationContext springContext;

    @Autowired
    private AggregatorRepository baseAggregatorRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Override
    public List<AggregatorData> getAllAggregatorsForApplication(Long applicationId, UserWrapper userWrapper) throws DataAccessException, ClassNotFoundException {
        Application application = applicationRepository.findById(applicationId).get();

        clientFilter.validateAccess(application, userWrapper.getUserApplications());

        List<Aggregator> allApplicationAggregators = baseAggregatorRepository.findByApplication( application);

        List<AggregatorData> concreteAggregators = new ArrayList<>();

        for(Aggregator aggregator : allApplicationAggregators) {
            CrudRepository aggregatorRepository = aggregatorReflection.getAggregatorRepository(aggregator, springContext);
            concreteAggregators.add(new AggregatorData((Aggregator) aggregatorRepository.findById(aggregator.getId()).get()));
        }

        return concreteAggregators;
    }

    @Override
    public AggregatorData saveNewAggregator(AggregatorData aggregatorData, UserWrapper userWrapper) throws DataAccessException, ClassNotFoundException {
        clientFilter.validateAccess(aggregatorData.getAggregator(), userWrapper.getUserApplications());

        Aggregator aggregator = aggregatorData.getAggregator();

        CrudRepository aggregatorRepository = aggregatorReflection.getAggregatorRepository(aggregator, springContext);

        return new AggregatorData((Aggregator) aggregatorRepository.save(aggregatorData.getAggregator()));
    }

    @Override
    public AggregatorData getEmptyAggregator(String aggregatorType) throws ClassNotFoundException {
        Aggregator aggregator = new Aggregator();
        //TODO: fix capitalize
//        aggregator.setAggregatorClass(capitalizeFirstLetter(aggregatorType) + AGGREGATOR_SUFFIX);

        aggregator = aggregatorReflection.createEmptyAggregator(aggregator);



        return new AggregatorData(aggregatorReflection.createEmptyAggregator(aggregator));
    }

    @Override
    public AggregatorData getAggregator(Long aggregatorId, UserWrapper userWrapper) throws DataAccessException, ClassNotFoundException {
        Aggregator aggregator = baseAggregatorRepository.findById(aggregatorId).get();

        clientFilter.validateAccess(aggregator, userWrapper.getUserApplications());

        CrudRepository aggregatorRepository = aggregatorReflection.getAggregatorRepository(aggregator, springContext);

        return new AggregatorData((Aggregator) aggregatorRepository.findById(aggregatorId).get());
    }

    @Override
    public AggregatorData updateAggregator(AggregatorData aggregatorData, UserWrapper userWrapper) throws DataAccessException, ClassNotFoundException {
        clientFilter.validateAccess(aggregatorData.getAggregator(), userWrapper.getUserApplications());

        CrudRepository aggregatorRepository = aggregatorReflection.getAggregatorRepository(aggregatorData.getAggregator(), springContext);

        return new AggregatorData((Aggregator) aggregatorRepository.save(aggregatorData.getAggregator()));
    }

    @Override
    public void removeAggregator(Long aggregatorId, UserWrapper userWrapper) throws DataAccessException, ClassNotFoundException {
        Aggregator aggregator = baseAggregatorRepository.findById(aggregatorId).get();

        clientFilter.validateAccess(aggregator, userWrapper.getUserApplications());

        CrudRepository aggregatorRepository = aggregatorReflection.getAggregatorRepository(aggregator, springContext);

        aggregatorRepository.delete(aggregatorId);
    }
}
