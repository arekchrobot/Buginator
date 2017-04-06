package pl.ark.chr.buginator.ext.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import pl.ark.chr.buginator.data.ExternalData;
import pl.ark.chr.buginator.ext.service.ExternalDataService;
import pl.ark.chr.buginator.rest.annotations.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Arek on 2017-04-01.
 */
@RestController("/ext/notify")
public class ExternalDataEndpoint {

    private static final Logger logger = LoggerFactory.getLogger(ExternalDataEndpoint.class);

    @Autowired
    private ExternalDataService externalDataService;

    @POST("/{uniqueKey}/{token}")
    public String receiveData(@PathVariable("uniqueKey") String uniqueKey, @PathVariable("token") String token,
                              @RequestBody ExternalData externalData,
                              HttpServletRequest request, HttpServletResponse response) {
        logger.info("Attempting to add extrnal data for company with unique key: " + uniqueKey + " and token: " + token + " with body: " + externalData.toString());

        try {
            externalDataService.saveErrorAndNotifyAggregators(uniqueKey, token, externalData);
        } catch (Exception ex) {
            logger.error("Error parsing received data: ", ex);
            return "ERROR";
        }

        return "OK";
    }
}
