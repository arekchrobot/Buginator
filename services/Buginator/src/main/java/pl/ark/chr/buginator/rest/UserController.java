package pl.ark.chr.buginator.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import pl.ark.chr.buginator.domain.auth.User;
import pl.ark.chr.buginator.app.exceptions.RestException;
import pl.ark.chr.buginator.app.exceptions.ValidationException;
import pl.ark.chr.buginator.rest.annotations.GET;
import pl.ark.chr.buginator.rest.annotations.POST;
import pl.ark.chr.buginator.rest.annotations.PUT;
import pl.ark.chr.buginator.rest.annotations.RestController;
import pl.ark.chr.buginator.service.UserService;
import pl.ark.chr.buginator.util.HttpUtil;
import pl.ark.chr.buginator.util.SessionUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Locale;

/**
 * Created by Arek on 2017-03-31.
 */
@RestController("/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private SessionUtil sessionUtil;

    @GET("/")
    public List<User> getAll(HttpServletRequest request, HttpServletResponse response) throws RestException {
        logger.info("Getting all users with user: " + sessionUtil.getCurrentUserEmail(request));

        return userService.getAllByCompany(sessionUtil.getCurrentUser(request).getCompany());
    }

    @GET("/{email}")
    public User get(@PathVariable("email") String email, HttpServletRequest request, HttpServletResponse response, Locale locale) throws RestException {
        logger.info("Getting user with email: " + email + " with user: " + sessionUtil.getCurrentUserEmail(request));

        return userService.loadUserByEmail(email, locale);
    }

    @POST("/")
    public User save(@RequestBody User user, HttpServletRequest request, HttpServletResponse response) throws RestException {
        logger.info("Saving user with id: " + user.getId() + " with user: " + sessionUtil.getCurrentUserEmail(request));

        try {
            return userService.save(user, sessionUtil.getCurrentUser(request).getCompany());
        } catch (ValidationException ex) {
            throw new RestException(ex.getLocalizedMessage(), HttpStatus.BAD_REQUEST, HttpUtil.generateOriginalUrl(request), user);
        }
    }

    @PUT("/{email}/{active}")
    public void activateDeactivate(@PathVariable("email") String email, @PathVariable("active") boolean active,HttpServletRequest request, HttpServletResponse response) throws RestException {
        if(active) {
            logger.info("Activating user with email: " + email + " with user: " + sessionUtil.getCurrentUserEmail(request));
        } else {
            logger.info("Deactivating user with email: " + email + " with user: " + sessionUtil.getCurrentUserEmail(request));
        }

        userService.activateDeactivateAccount(email, sessionUtil.getCurrentUser(request).getCompany(), active);
    }
}
