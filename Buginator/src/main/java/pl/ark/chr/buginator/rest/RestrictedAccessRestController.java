package pl.ark.chr.buginator.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import pl.ark.chr.buginator.domain.BaseEntity;
import pl.ark.chr.buginator.domain.filter.FilterData;
import pl.ark.chr.buginator.exceptions.DataAccessException;
import pl.ark.chr.buginator.exceptions.RestException;
import pl.ark.chr.buginator.filter.ClientFilter;
import pl.ark.chr.buginator.rest.annotations.DELETE;
import pl.ark.chr.buginator.rest.annotations.GET;
import pl.ark.chr.buginator.rest.annotations.POST;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by Arek on 2016-12-01.
 */
public abstract class RestrictedAccessRestController<T extends BaseEntity & FilterData> extends CrudRestController<T> {

    private final static Logger logger = LoggerFactory.getLogger(RestrictedAccessRestController.class);

    protected abstract ClientFilter getClientFilter();

    @Override
    @GET("/")
    public List<T> getAll(HttpServletRequest request) throws RestException {
        logger.info("User: " + getHttpSessionUtil().getCurrentUser(request).getUsername() + " tries to get all restricted resources");
        throw new DataAccessException("Attempt to access forbidden resources", "");
    }

    @Override
    @GET("/{id}")
    public T get(@PathVariable("id") Long id, HttpServletRequest request) throws RestException {
        T filterData = super.get(id, request);
        getClientFilter().validateAccess(filterData, getHttpSessionUtil().getCurrentUser(request).getUserApplications());
        return filterData;
    }

    @Override
    @POST("/")
    public T save(@RequestBody T entity, HttpServletRequest request) throws RestException {
        getClientFilter().validateAccess(entity, getHttpSessionUtil().getCurrentUser(request).getUserApplications());
        return super.save(entity, request);
    }

    @Override
    @DELETE("/{id}")
    public void delete(@PathVariable("id") Long id, HttpServletRequest request) throws RestException {
        T filterData = getService().get(id);
        getClientFilter().validateAccess(filterData, getHttpSessionUtil().getCurrentUser(request).getUserApplications());
        super.delete(id, request);
    }
}
