package pl.ark.chr.buginator.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import pl.ark.chr.buginator.data.ErrorWrapper;
import pl.ark.chr.buginator.domain.core.Error;
import pl.ark.chr.buginator.exceptions.RestException;
import pl.ark.chr.buginator.rest.annotations.GET;
import pl.ark.chr.buginator.rest.annotations.RestController;
import pl.ark.chr.buginator.service.ErrorService;
import pl.ark.chr.buginator.service.RestrictedAccessCrudService;
import pl.ark.chr.buginator.util.SessionUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Arek on 2017-01-16.
 */
@RestController("/error")
public class ErrorController extends RestrictedAccessRestController<Error> {

    private final static Logger logger = LoggerFactory.getLogger(ErrorController.class);

    @Autowired
    private SessionUtil sessionUtil;

    @Autowired
    private ErrorService errorService;

    @Override
    protected SessionUtil getHttpSessionUtil() {
        return sessionUtil;
    }

    @Override
    protected RestrictedAccessCrudService<Error> getService() {
        return errorService;
    }

    @Override
    @GET("/notUsed/{id}")
    public Error get(@PathVariable("id") Long id, HttpServletRequest request) throws RestException {
        throw new UnsupportedOperationException("Use method that returns ErrorWrapper: getOne()");
    }

    @GET("/{id}")
    public ErrorWrapper getOne(@PathVariable("id") Long id, HttpServletRequest request) throws RestException {
        logger.info("Getting " + className + " with id: " + id + " with user: " + getHttpSessionUtil().getCurrentUserEmail(request));
        Error entity = getService().get(id, getUserApplications(request));

        return new ErrorWrapper(entity);
    }
}
