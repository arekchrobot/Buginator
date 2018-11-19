//package pl.ark.chr.buginator.aggregator.util;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.context.ApplicationContext;
//import org.springframework.data.repository.CrudRepository;
//import org.springframework.stereotype.Component;
//import pl.ark.chr.buginator.aggregator.service.AggregatorService;
//import pl.ark.chr.buginator.domain.aggregator.Aggregator;
//
//import java.lang.reflect.Constructor;
//import java.lang.reflect.InvocationTargetException;
//
///**
// * Created by Arek on 2017-03-11.
// */
//@Component
//public class AggregatorReflection {
//
//    private static final Logger logger = LoggerFactory.getLogger(AggregatorReflection.class);
//
//    private static final String REPOSITORY_PKG_PREFIX = "pl.ark.chr.buginator.repository.";
//    private static final String REPOSITORY_PKG_SUFFIX = "Repository";
//
//    private static final String ENTITY_PKG_PREFIX = "pl.ark.chr.buginator.domain.";
//
//    private static final String SERVICE_PKG_PREFIX = "pl.ark.chr.buginator.aggregator.service.impl.";
//    private static final String SERVICE_PKG_SUFFIX = "ServiceImpl";
//
//    public CrudRepository getAggregatorRepository(Aggregator aggregator, ApplicationContext springContext) throws ClassNotFoundException {
//        Class aggregatorClazz;
//
//        try {
//            aggregatorClazz = Class.forName(REPOSITORY_PKG_PREFIX + aggregator.getAggregatorClass() + REPOSITORY_PKG_SUFFIX);
//        } catch (ClassNotFoundException e) {
//            logger.warn("No aggregator repository found for class: " + aggregator.getAggregatorClass());
//
//            aggregatorClazz = Class.forName(REPOSITORY_PKG_PREFIX + "Aggregator" + REPOSITORY_PKG_SUFFIX);
//        }
//
//        CrudRepository aggregatorRepository = (CrudRepository) springContext.getBean(aggregatorClazz);
//
//        return aggregatorRepository;
//    }
//
//    public Aggregator createEmptyAggregator(Aggregator aggregator) {
//        try {
//            Class aggregatorEntity = Class.forName(ENTITY_PKG_PREFIX + aggregator.getAggregatorClass());
//
//            Constructor aggregatorConstructor = aggregatorEntity.getDeclaredConstructor();
//            aggregatorConstructor.setAccessible(true);
//
//            return (Aggregator) aggregatorConstructor.newInstance();
//        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
//            logger.error("No aggregator entity found for class: " + aggregator.getAggregatorClass());
//
//            return aggregator;
//        }
//    }
//
//    public AggregatorService getAggregatorService(Aggregator aggregator, ApplicationContext springContext) throws ClassNotFoundException {
//        Class aggregatorClazz;
//
//        try {
//            aggregatorClazz = Class.forName(SERVICE_PKG_PREFIX + aggregator.getAggregatorClass() + SERVICE_PKG_SUFFIX);
//        } catch (ClassNotFoundException e) {
//            logger.warn("No aggregator repository found for class: " + aggregator.getAggregatorClass());
//
//            aggregatorClazz = null;
//        }
//
//        AggregatorService service = (agg, error) -> logger.warn("Empty implementation of aggregator service for class: " + agg.getAggregatorClass());
//
//        if (aggregatorClazz != null) {
//
//            service = (AggregatorService) springContext.getBean(aggregatorClazz);
//        }
//
//        return service;
//    }
//}
