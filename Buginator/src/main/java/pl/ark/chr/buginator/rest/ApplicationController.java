package pl.ark.chr.buginator.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import pl.ark.chr.buginator.domain.Application;
import pl.ark.chr.buginator.domain.UserApplication;
import pl.ark.chr.buginator.exceptions.RestException;
import pl.ark.chr.buginator.rest.annotations.GET;
import pl.ark.chr.buginator.rest.annotations.POST;
import pl.ark.chr.buginator.rest.annotations.RestController;
import pl.ark.chr.buginator.service.ApplicationService;
import pl.ark.chr.buginator.service.RestrictedAccessCrudService;
import pl.ark.chr.buginator.util.SessionUtil;
import pl.ark.chr.buginator.data.UserWrapper;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by Arek on 2016-12-03.
 */
@RestController("/application")
public class ApplicationController extends RestrictedAccessRestController<Application> {

    private final static Logger logger = LoggerFactory.getLogger(ApplicationController.class);

    @Autowired
    private SessionUtil sessionUtil;

    @Autowired
    private ApplicationService applicationService;

    @Override
    protected RestrictedAccessCrudService<Application> getService() {
        return applicationService;
    }

    @Override
    protected SessionUtil getHttpSessionUtil() {
        return sessionUtil;
    }

    @GET("/")
    public List<Application> getAll(HttpServletRequest request) throws RestException {
        logger.info("Getting all applications for user: " + getHttpSessionUtil().getCurrentUserEmail(request));
        return applicationService.getUserApplications(getHttpSessionUtil().getCurrentUser(request));
    }

    @Override
    @POST("/")
    public Application save(@RequestBody Application entity, HttpServletRequest request) throws RestException {
        logger.info("Saving " + className + " with id: " + entity.getId() + " with user: " + getHttpSessionUtil().getCurrentUserEmail(request));

        UserWrapper userWrapper = getHttpSessionUtil().getCurrentUser(request);
        UserApplication newUserApplication = applicationService.save(entity, userWrapper);

        userWrapper.getUserApplications().add(newUserApplication);
        getHttpSessionUtil().setCurrentUser(request, userWrapper);

        return newUserApplication.getApplication();
    }
}
