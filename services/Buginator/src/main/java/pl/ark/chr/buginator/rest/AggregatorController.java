package pl.ark.chr.buginator.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import pl.ark.chr.buginator.data.AggregatorData;
import pl.ark.chr.buginator.app.exceptions.RestException;
import pl.ark.chr.buginator.rest.annotations.*;
import pl.ark.chr.buginator.service.AggregatorService;
import pl.ark.chr.buginator.util.SessionUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by Arek on 2017-03-15.
 */
@RestController("/aggregator")
public class AggregatorController {

    private final static Logger logger = LoggerFactory.getLogger(AggregatorController.class);

    @Autowired
    private AggregatorService aggregatorService;

    @Autowired
    private SessionUtil sessionUtil;

    @GET("/byApplication/{id}")
    public List<AggregatorData> getAllByApplication(@PathVariable("id") Long appId, HttpServletRequest request) throws RestException, ClassNotFoundException {
        logger.info("Getting all aggregators for application: " + appId + " for user: " + sessionUtil.getCurrentUserEmail(request));
        return aggregatorService.getAllAggregatorsForApplication(appId, sessionUtil.getCurrentUser(request));
    }

    @GET("/{id}")
    public AggregatorData get(@PathVariable("id") Long aggregatorId, HttpServletRequest request) throws RestException, ClassNotFoundException {
        logger.info("Getting aggregator with id: " + aggregatorId + " for user: " + sessionUtil.getCurrentUserEmail(request));
        return aggregatorService.getAggregator(aggregatorId, sessionUtil.getCurrentUser(request));
    }

    @GET("/empty/{type}")
    public AggregatorData getEmpty(@PathVariable("type") String aggregatorType, HttpServletRequest request) throws RestException, ClassNotFoundException {
        logger.info("Creating empty aggregator with type: " + aggregatorType + " for user: " + sessionUtil.getCurrentUserEmail(request));
        return aggregatorService.getEmptyAggregator(aggregatorType);
    }

    @POST("/")
    public AggregatorData save(@RequestBody AggregatorData aggregatorData, HttpServletRequest request) throws RestException, ClassNotFoundException {
        logger.info("Creating new aggregator with user: " + sessionUtil.getCurrentUserEmail(request));
        return aggregatorService.saveNewAggregator(aggregatorData, sessionUtil.getCurrentUser(request));
    }

    @PUT("/")
    public AggregatorData update(@RequestBody AggregatorData aggregatorData, HttpServletRequest request) throws RestException, ClassNotFoundException {
        logger.info("Updating aggregator with id: " + aggregatorData.getAggregator().getId() + " with user: " + sessionUtil.getCurrentUserEmail(request));
        return aggregatorService.updateAggregator(aggregatorData, sessionUtil.getCurrentUser(request));
    }

    @DELETE("/{id}")
    public void delete(@PathVariable("id") Long aggregatorId, HttpServletRequest request) throws RestException, ClassNotFoundException {
        logger.info("Deleting aggregator with id: " + aggregatorId + " with user: " + sessionUtil.getCurrentUserEmail(request));
        aggregatorService.removeAggregator(aggregatorId, sessionUtil.getCurrentUser(request));
    }
}
