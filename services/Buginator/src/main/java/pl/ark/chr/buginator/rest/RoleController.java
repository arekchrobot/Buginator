package pl.ark.chr.buginator.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import pl.ark.chr.buginator.domain.auth.Permission;
import pl.ark.chr.buginator.domain.auth.Role;
import pl.ark.chr.buginator.exceptions.RestException;
import pl.ark.chr.buginator.exceptions.ValidationException;
import pl.ark.chr.buginator.rest.annotations.DELETE;
import pl.ark.chr.buginator.rest.annotations.GET;
import pl.ark.chr.buginator.rest.annotations.POST;
import pl.ark.chr.buginator.rest.annotations.RestController;
import pl.ark.chr.buginator.service.RoleService;
import pl.ark.chr.buginator.util.HttpUtil;
import pl.ark.chr.buginator.util.SessionUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by Arek on 2017-03-22.
 */
@RestController("/role")
public class RoleController {

    private final static Logger logger = LoggerFactory.getLogger(RoleController.class);

    @Autowired
    private RoleService roleService;

    @Autowired
    private SessionUtil sessionUtil;

    @GET("/")
    public List<Role> getAll(HttpServletRequest request, HttpServletResponse response) throws RestException {
        logger.info("Getting all roles with user: " + sessionUtil.getCurrentUserEmail(request));

        return roleService.getAll(sessionUtil.getCurrentUser(request).getCompany());
    }

    @GET("/{id}")
    public Role get(@PathVariable("id") Long id, HttpServletRequest request, HttpServletResponse response) throws RestException {
        logger.info("Get role with id: " + id + " with user: " + sessionUtil.getCurrentUserEmail(request));

        try {
            return roleService.get(id, sessionUtil.getCurrentUser(request).getCompany());
        } catch (ValidationException ex) {
            logger.warn("Unauthorized access to role: " + id + " with user: " + sessionUtil.getCurrentUserEmail(request));
            throw new RestException(ex.getLocalizedMessage(), HttpStatus.BAD_REQUEST, HttpUtil.generateOriginalUrl(request));
        }
    }

    @GET("/perms/all")
    public List<Permission> getAllPermissions(HttpServletRequest request, HttpServletResponse response) throws RestException {
        logger.info("Get all permissions with user: " + sessionUtil.getCurrentUserEmail(request));

        return roleService.getAllPermissions();
    }

    @POST("/")
    public Role save(@RequestBody Role role, HttpServletRequest request, HttpServletResponse response) throws RestException {
        logger.info("Saving role with id: " + role.getId() + " with user: " + sessionUtil.getCurrentUserEmail(request));

        try {
            return roleService.save(role, sessionUtil.getCurrentUser(request).getCompany());
        } catch (ValidationException ex) {
            logger.warn("Unauthorized save of role: " + role.getId() + " with user: " + sessionUtil.getCurrentUserEmail(request));
            throw new RestException(ex.getLocalizedMessage(), HttpStatus.BAD_REQUEST, HttpUtil.generateOriginalUrl(request), role);
        }
    }

    @DELETE("/{id}")
    public void delete(@PathVariable("id") Long id, HttpServletRequest request, HttpServletResponse response) throws RestException {
        logger.info("Deleting role with id: " + id + " with user: " + sessionUtil.getCurrentUserEmail(request));

        try {
            roleService.delete(id, sessionUtil.getCurrentUser(request).getCompany());
        } catch (ValidationException ex) {
            logger.warn("Unauthorized remove of role: " + id + " with user: " + sessionUtil.getCurrentUserEmail(request));
            throw new RestException(ex.getLocalizedMessage(), HttpStatus.BAD_REQUEST, HttpUtil.generateOriginalUrl(request));
        }
    }
}
