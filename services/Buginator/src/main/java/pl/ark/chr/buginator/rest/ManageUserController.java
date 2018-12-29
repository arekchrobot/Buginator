package pl.ark.chr.buginator.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import pl.ark.chr.buginator.data.ManageUserData;
import pl.ark.chr.buginator.exceptions.RestException;
import pl.ark.chr.buginator.rest.annotations.*;
import pl.ark.chr.buginator.service.ManageUserService;
import pl.ark.chr.buginator.util.SessionUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Arek on 2017-02-04.
 */
@RestController("/manageUser")
public class ManageUserController {

    private final static Logger logger = LoggerFactory.getLogger(ManageUserController.class);

    private final static String RESPONSE_OK = "OK";
    private final static String RESPONSE_ERROR = "ERROR";

    @Autowired
    private ManageUserService manageUserService;

    @Autowired
    private SessionUtil sessionUtil;

    @Autowired
    private MessageSource messageSource;

    @GET("/byApplication/{id}")
    public List<ManageUserData> getUsersByApplication(@PathVariable("id") Long appId, HttpServletRequest request) throws RestException {
        logger.info("Getting all users with access to application: " + appId + " with user: " + sessionUtil.getCurrentUserEmail(request));

        return manageUserService.getAllApplicationUsers(appId, sessionUtil.getCurrentUser(request).getUserApplications());
    }

    @GET("/byApplicationNot/{id}")
    public List<ManageUserData> getUsersByApplicationNot(@PathVariable("id") Long appId, HttpServletRequest request) throws RestException {
        logger.info("Getting all users with no access to application: " + appId + " with user: " + sessionUtil.getCurrentUserEmail(request));

        return manageUserService.getAllUsersNotInApplication(appId, sessionUtil.getCurrentUser(request));
    }

    @POST("/{id}")
    public Map addUsersToApplication(@RequestBody List<ManageUserData> usersToAdd, @PathVariable("id") Long appId, HttpServletRequest request) throws RestException {
        StringBuilder loggerBuilder = new StringBuilder(100);
        loggerBuilder.append("Adding users: ");
        usersToAdd.forEach(user -> loggerBuilder.append(user.getEmail()).append(", "));
        loggerBuilder.append("to application: ").append(appId);

        logger.info(loggerBuilder.toString());

        try {
            manageUserService.addUsersToApplication(usersToAdd, appId, sessionUtil.getCurrentUser(request).getUserApplications());
        } catch (Exception ex) {
            logger.error("Error adding users to application: " + appId, ex);
            return Collections.singletonMap("result", RESPONSE_ERROR);
        }

        return Collections.singletonMap("result", RESPONSE_OK);
    }

    @DELETE("/{id}")
    public Map removeUsersFromApplication(@RequestBody List<ManageUserData> usersToRemove, @PathVariable("id") Long appId, HttpServletRequest request) throws RestException {
        StringBuilder loggerBuilder = new StringBuilder(100);
        loggerBuilder.append("Removing users: ");
        usersToRemove.forEach(user -> loggerBuilder.append(user.getEmail()).append(", "));
        loggerBuilder.append("from application: ").append(appId);

        logger.info(loggerBuilder.toString());

        try {
            manageUserService.removeUsersFromApplication(usersToRemove, appId, sessionUtil.getCurrentUser(request).getUserApplications());
        } catch (Exception ex) {
            logger.error("Error removing users from application: " + appId, ex);
            return Collections.singletonMap("result", RESPONSE_ERROR);
        }

        return Collections.singletonMap("result", RESPONSE_OK);
    }

    @PUT("/{id}")
    public Map changeUserPermissionToApp(@RequestBody ManageUserData userData, @PathVariable("id") Long appId, Locale locale, HttpServletRequest request) throws RestException {
        logger.info("Changing permission to application: " + appId + " for user: " + userData.getEmail());

        try {
            manageUserService.changeAccessToAppForUser(userData, appId, sessionUtil.getCurrentUser(request).getUserApplications());
        } catch (Exception ex) {
            logger.error("Error changing permission to application: " + appId + " for user: " + userData.getEmail());
            return Collections.singletonMap("result", messageSource.getMessage("alterPermissionToApp.msg", null, locale));
        }

        return Collections.singletonMap("result", RESPONSE_OK);
    }
}
