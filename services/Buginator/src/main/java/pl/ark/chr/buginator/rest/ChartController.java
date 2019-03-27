package pl.ark.chr.buginator.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import pl.ark.chr.buginator.data.ChartData;
import pl.ark.chr.buginator.exceptions.ChartException;
import pl.ark.chr.buginator.exceptions.RestException;
import pl.ark.chr.buginator.core.security.filter.ClientFilter;
import pl.ark.chr.buginator.core.security.filter.ClientFilterFactory;
import pl.ark.chr.buginator.rest.annotations.GET;
import pl.ark.chr.buginator.rest.annotations.RestController;
import pl.ark.chr.buginator.service.ChartService;
import pl.ark.chr.buginator.util.HttpUtil;
import pl.ark.chr.buginator.util.SessionUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Arek on 2016-12-25.
 */
@RestController("/chart")
public class ChartController {

    private final static Logger logger = LoggerFactory.getLogger(ApplicationController.class);

    private final ClientFilter clientFilter = ClientFilterFactory.createClientFilter(ClientFilterFactory.ClientFilterType.APPLICATION_ACCESS);

    @Autowired
    private SessionUtil sessionUtil;

    @Autowired
    private ChartService chartService;

    @GET("/applicationLastWeek/{id}")
    public ChartData generateLastWeekErrorChartForApplication(@PathVariable("id") Long id, HttpServletRequest request) throws RestException {
        logger.info("Getting last week error count for application: " + id + " with user: " + sessionUtil.getCurrentUserEmail(request));

        try {
            return chartService.generateLastWeekErrorsForApplication(id, sessionUtil.getCurrentUser(request).getUserApplications());
        } catch (ChartException e) {
            throw new RestException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, HttpUtil.generateOriginalUrl(request));
        }
    }
}
