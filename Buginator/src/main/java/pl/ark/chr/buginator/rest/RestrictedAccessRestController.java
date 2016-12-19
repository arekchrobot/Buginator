package pl.ark.chr.buginator.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pl.ark.chr.buginator.domain.BaseEntity;
import pl.ark.chr.buginator.domain.filter.FilterData;
import pl.ark.chr.buginator.exceptions.DataAccessException;
import pl.ark.chr.buginator.exceptions.RestException;
import pl.ark.chr.buginator.filter.ClientFilter;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by Arek on 2016-12-01.
 */
public abstract class RestrictedAccessRestController<T extends BaseEntity & FilterData> extends CrudRestController<T> {

    private final static Logger logger = LoggerFactory.getLogger(RestrictedAccessRestController.class);

    protected abstract ClientFilter getClientFilter();

    @Override
    @RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<T> getAll(HttpServletRequest request) throws RestException {
        logger.info("User: " + getHttpSessionUtil().getCurrentUser(request).getUsername() + " tries to get all restricted resources");
        throw new DataAccessException("Attempt to access forbidden resources", "");
    }

    @Override
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public T get(@PathVariable("id") Long id, HttpServletRequest request) throws RestException {
        T filterData = super.get(id, request);
        getClientFilter().validateAccess(filterData, getHttpSessionUtil().getCurrentUser(request).getUserApplications());
        return filterData;
    }

    @Override
    @RequestMapping(value = "/", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public T save(@RequestBody T entity, HttpServletRequest request) throws RestException {
        getClientFilter().validateAccess(entity, getHttpSessionUtil().getCurrentUser(request).getUserApplications());
        return super.save(entity, request);
    }

    @Override
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void delete(@PathVariable("id") Long id, HttpServletRequest request) throws RestException {
        T filterData = getService().get(id);
        getClientFilter().validateAccess(filterData, getHttpSessionUtil().getCurrentUser(request).getUserApplications());
        super.delete(id, request);
    }
}
