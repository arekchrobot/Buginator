package pl.ark.chr.buginator.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RestController;
import pl.ark.chr.buginator.domain.Application;
import pl.ark.chr.buginator.domain.UserApplication;
import pl.ark.chr.buginator.exceptions.RestException;
import pl.ark.chr.buginator.filter.ClientFilter;
import pl.ark.chr.buginator.filter.ClientFilterFactory;
import pl.ark.chr.buginator.rest.annotations.GET;
import pl.ark.chr.buginator.rest.annotations.POST;
import pl.ark.chr.buginator.rest.annotations.RestController;
import pl.ark.chr.buginator.service.ApplicationService;
import pl.ark.chr.buginator.service.CrudService;
import pl.ark.chr.buginator.util.SessionUtil;
import pl.ark.chr.buginator.util.UserWrapper;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Arek on 2016-12-03.
 */
@RestController("/application")
public class ApplicationController extends RestrictedAccessRestController<Application> {

    private final static Logger logger = LoggerFactory.getLogger(ApplicationController.class);

    private final ClientFilter clientFilter = ClientFilterFactory.createClientFilter(ClientFilterFactory.ClientFilterType.APPLICATION_ACCESS,
            ClientFilterFactory.ClientFilterType.DATA_MODIFY);

    @Autowired
    private SessionUtil sessionUtil;

    @Autowired
    private ApplicationService applicationService;

    @Override
    protected ClientFilter getClientFilter() {
        return clientFilter;
    }

    @Override
    protected CrudService<Application> getService() {
        return applicationService;
    }

    @Override
    protected SessionUtil getHttpSessionUtil() {
        return sessionUtil;
    }

    @Override
    @GET("/")
    public List<Application> getAll(HttpServletRequest request) throws RestException {
        logger.info("Getting all applications for user: " + getHttpSessionUtil().getCurrentUser(request).getUsername());
        return applicationService.getUserApplications(getHttpSessionUtil().getCurrentUser(request));
    }

    @Override
    @POST("/")
    public Application save(@RequestBody Application entity, HttpServletRequest request) throws RestException {
        logger.info("Saving " + className + " with id: " + entity.getId() + " with user: " + getHttpSessionUtil().getCurrentUser(request).getUsername());

        if(!entity.isNew()) {
            getClientFilter().validateAccess(entity, getHttpSessionUtil().getCurrentUser(request).getUserApplications());
        }

        UserWrapper userWrapper = getHttpSessionUtil().getCurrentUser(request);
        UserApplication newUserApplication = applicationService.save(entity, userWrapper);

        userWrapper.getUserApplications().add(newUserApplication);
        getHttpSessionUtil().setCurrentUser(request, userWrapper);

        return newUserApplication.getApplication();
    }
}
