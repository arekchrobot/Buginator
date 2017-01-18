package pl.ark.chr.buginator.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import pl.ark.chr.buginator.domain.Error;
import pl.ark.chr.buginator.rest.annotations.RestController;
import pl.ark.chr.buginator.service.ErrorService;
import pl.ark.chr.buginator.service.RestrictedAccessCrudService;
import pl.ark.chr.buginator.util.SessionUtil;

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
}
