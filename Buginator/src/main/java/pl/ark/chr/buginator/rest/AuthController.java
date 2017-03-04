package pl.ark.chr.buginator.rest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import pl.ark.chr.buginator.data.Credentials;
import pl.ark.chr.buginator.data.RegisterData;
import pl.ark.chr.buginator.data.UserWrapper;
import pl.ark.chr.buginator.domain.User;
import pl.ark.chr.buginator.exceptions.RestException;
import pl.ark.chr.buginator.exceptions.UsernameNotFoundException;
import pl.ark.chr.buginator.exceptions.ValidationException;
import pl.ark.chr.buginator.rest.annotations.GET;
import pl.ark.chr.buginator.rest.annotations.POST;
import pl.ark.chr.buginator.rest.annotations.PUT;
import pl.ark.chr.buginator.rest.annotations.RestController;
import pl.ark.chr.buginator.service.NotificationService;
import pl.ark.chr.buginator.service.RegisterService;
import pl.ark.chr.buginator.service.UserService;
import pl.ark.chr.buginator.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Arek on 2016-09-29.
 */
@RestController("/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private SessionUtil sessionUtil;

    @Autowired
    private RegisterService registerService;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private NotificationService notificationService;

    @POST("/login")
    public UserWrapper login(HttpServletRequest request, HttpServletResponse response, Locale locale, @RequestBody Credentials credentials) throws RestException {
        UsernamePasswordToken authToken = new UsernamePasswordToken(credentials.getUsername().toLowerCase(), credentials.getPassword(), true);
        try {
            logger.info("Logging user: " + credentials.getUsername());

            SecurityUtils.getSubject().login(authToken);
            User user = userService.validateUserLogin(credentials, locale);
            UserWrapper userWrapper = new UserWrapper(user, notificationService.addTokenForActiveSession(user));
            sessionUtil.setCurrentUser(request, userWrapper);
            return userWrapper;
        } catch (AuthenticationException exception) {
            logger.info("Not user found with username: " + credentials.getUsername() + " and password: " + credentials.getPassword());
            throw new RestException(messageSource.getMessage("wrongCredentials.msg", null, locale), HttpStatus.UNAUTHORIZED, HttpUtil.generateOriginalUrl(request), credentials);
        } catch (RuntimeException ex) {
            logger.info("Error executing login for user: " + credentials.getUsername() + " with error" + ex.getMessage());
            throw new RestException(ex.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR, HttpUtil.generateOriginalUrl(request), credentials);
        }
    }

    @POST("/signout")
    public boolean logout(HttpServletRequest request, HttpServletResponse response) {
        UserWrapper currentUser = sessionUtil.getCurrentUser(request);
        String username = currentUser != null ? currentUser.getEmail() : "";
        String token = currentUser != null ? currentUser.getToken() : "";
        logger.info("Logging out user: " + username);
        notificationService.removeTokenForSession(token);
        SecurityUtils.getSubject().logout();
        sessionUtil.removeCurrentUser(request);
        return true;
    }

    @GET("/logged")
    public UserWrapper isLogged(HttpServletRequest request, HttpServletResponse response) {
        return sessionUtil.getCurrentUser(request);
    }

    @POST("/register")
    public boolean registerUser(HttpServletRequest request, HttpServletResponse response, Locale locale, @RequestBody RegisterData registerData) throws RestException {
        logger.info("Creating new company: " + registerData.getCompany().getName() + " with user: " + registerData.getUser().getEmail());
        try {
            registerService.registerUser(registerData, locale);
        } catch (ValidationException | IllegalArgumentException ex) {
            throw new RestException(ex.getLocalizedMessage(), HttpStatus.BAD_REQUEST, HttpUtil.generateOriginalUrl(request), registerData);
        }
        return true;
    }

    @PUT("/reset")
    public Map resetPassword(HttpServletRequest request, HttpServletResponse response, Locale locale, @RequestParam("lg") String userEmail) throws RestException {
        logger.info("Resseting password for user: " + userEmail);
        try {
            userService.resetPassword(userEmail, locale);
        } catch (UsernameNotFoundException ex) {
            throw new RestException(ex.getLocalizedMessage(), HttpStatus.BAD_REQUEST, HttpUtil.generateOriginalUrl(request), null);
        }
        return Collections.singletonMap("success", "Password reset successfully");
    }
}
