package pl.ark.chr.buginator.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pl.ark.chr.buginator.domain.BaseEntity;
import pl.ark.chr.buginator.exceptions.RestException;
import pl.ark.chr.buginator.service.CrudService;
import pl.ark.chr.buginator.util.HttpUtil;
import pl.ark.chr.buginator.util.SessionUtil;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.ParameterizedType;
import java.util.List;

/**
 * Created by Arek on 2016-09-29.
 */
public abstract class CrudRestController<T extends BaseEntity> {

    private final static Logger logger = LoggerFactory.getLogger(CrudRestController.class);
    protected String className;

    @PostConstruct
    private void init() {
        ParameterizedType thisType = (ParameterizedType) getClass().getGenericSuperclass();
        Class<T> domainClass = (Class<T>) thisType.getActualTypeArguments()[0];
        this.className = domainClass.getSimpleName();
    }

    protected abstract CrudService<T> getService();

    protected abstract SessionUtil getHttpSessionUtil();

    @RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<T> getAll(HttpServletRequest request) throws RestException {
//        try {
            logger.info("Getting all " + className + " with user: " + getHttpSessionUtil().getCurrentUser(request).getUsername());
            return getService().getAll();
//        } catch (RuntimeException ex) {
//            logger.error(ex.getMessage(), ex);
//            throw new RestException(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, HttpUtil.generateOriginalUrl(request));
//        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public T get(@PathVariable("id") Long id, HttpServletRequest request) throws RestException {
//        try {
            logger.info("Getting " + className + " with id: " + id + " with user: " + getHttpSessionUtil().getCurrentUser(request).getUsername());
            return getService().get(id);
//        } catch (RuntimeException ex) {
//            logger.error(ex.getMessage(), ex);
//            throw new RestException(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, HttpUtil.generateOriginalUrl(request));
//        }
    }

    @RequestMapping(value = "/", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public T save(@RequestBody T entity, HttpServletRequest request) throws RestException {
//        try {
            logger.info("Saving " + className + " with id: " + entity.getId() + " with user: " + getHttpSessionUtil().getCurrentUser(request).getUsername());
            return getService().save(entity);
//        } catch (RuntimeException ex) {
//            logger.error(ex.getMessage(), ex);
//            throw new RestException(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, HttpUtil.generateOriginalUrl(request), entity);
//        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void delete(@PathVariable("id") Long id, HttpServletRequest request) {
        logger.info("Deleting " + className + " with id: " + id + " with user: " + getHttpSessionUtil().getCurrentUser(request).getUsername());
        getService().delete(id);
    }
}
