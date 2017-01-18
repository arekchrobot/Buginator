package pl.ark.chr.buginator.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import pl.ark.chr.buginator.domain.BaseEntity;
import pl.ark.chr.buginator.domain.UserApplication;
import pl.ark.chr.buginator.domain.filter.FilterData;
import pl.ark.chr.buginator.exceptions.RestException;
import pl.ark.chr.buginator.rest.annotations.DELETE;
import pl.ark.chr.buginator.rest.annotations.GET;
import pl.ark.chr.buginator.rest.annotations.POST;
import pl.ark.chr.buginator.service.RestrictedAccessCrudService;
import pl.ark.chr.buginator.util.SessionUtil;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Set;

/**
 * Created by Arek on 2017-01-18.
 */
public abstract class RestrictedAccessRestController<T extends BaseEntity & FilterData>{

    private final static Logger logger = LoggerFactory.getLogger(RestrictedAccessRestController.class);
    protected String className;

    @PostConstruct
    private void init() {
        ParameterizedType thisType = (ParameterizedType) getClass().getGenericSuperclass();
        Class<T> domainClass = (Class<T>) thisType.getActualTypeArguments()[0];
        this.className = domainClass.getSimpleName();
    }

    protected abstract SessionUtil getHttpSessionUtil();

    protected abstract RestrictedAccessCrudService<T> getService();

    @GET("/byApplication/{id}")
    public List<T> getAllByApplication(@PathVariable("id") Long id, HttpServletRequest request) throws RestException {
        logger.info("Getting all " + className + " by application with id: " + id + " with user: " + getHttpSessionUtil().getCurrentUser(request).getEmail());
        return getService().getAllByApplication(id, getUserApplications(request));
    }

    @GET("/{id}")
    public T get(@PathVariable("id") Long id, HttpServletRequest request) throws RestException {
        logger.info("Getting " + className + " with id: " + id + " with user: " + getHttpSessionUtil().getCurrentUser(request).getEmail());
        return getService().get(id, getUserApplications(request));
    }

    @POST("/")
    public T save(@RequestBody T entity, HttpServletRequest request) throws RestException {
        logger.info("Saving " + className + " with id: " + entity.getId() + " with user: " + getHttpSessionUtil().getCurrentUser(request).getEmail());
        return getService().save(entity, getUserApplications(request));
    }

    @DELETE("/{id}")
    public void delete(@PathVariable("id") Long id, HttpServletRequest request) throws RestException {
        logger.info("Deleting " + className + " with id: " + id + " with user: " + getHttpSessionUtil().getCurrentUser(request).getEmail());
        getService().delete(id, getUserApplications(request));
    }

    protected Set<UserApplication> getUserApplications(HttpServletRequest request) {
        return getHttpSessionUtil().getCurrentUser(request).getUserApplications();
    }
}
