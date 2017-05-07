package pl.ark.chr.buginator.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import pl.ark.chr.buginator.domain.AggregatorLog;
import pl.ark.chr.buginator.exceptions.RestException;
import pl.ark.chr.buginator.rest.annotations.GET;
import pl.ark.chr.buginator.rest.annotations.RestController;
import pl.ark.chr.buginator.service.AggregatorLogService;
import pl.ark.chr.buginator.util.SessionUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by Arek on 2017-04-06.
 */
@RestController("/aggregatorLog")
public class AggregatorLogController {

    private static final Logger logger = LoggerFactory.getLogger(AggregatorLogController.class);

    @Autowired
    private AggregatorLogService aggregatorLogService;

    @Autowired
    private SessionUtil sessionUtil;

    @GET("/{id}")
    public List<AggregatorLog> getAllByAggregator(@PathVariable("id") Long id, HttpServletRequest request) throws RestException {
        logger.info("Getting all aggregator logs for aggregator: " + id + " with user: " + sessionUtil.getCurrentUserEmail(request));

        return aggregatorLogService.getAllByAggregator(id, sessionUtil.getCurrentUser(request).getUserApplications());
    }
}
