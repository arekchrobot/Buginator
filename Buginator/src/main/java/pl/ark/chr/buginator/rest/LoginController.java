package pl.ark.chr.buginator.rest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.ark.chr.buginator.domain.User;
import pl.ark.chr.buginator.exceptions.RestException;
import pl.ark.chr.buginator.service.UserService;
import pl.ark.chr.buginator.util.Credentials;
import pl.ark.chr.buginator.util.HttpUtil;
import pl.ark.chr.buginator.util.SessionUtil;
import pl.ark.chr.buginator.util.UserWrapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Arek on 2016-09-29.
 */
@RestController
@RequestMapping("/auth")
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private SessionUtil sessionUtil;

    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public UserWrapper login(HttpServletRequest request, HttpServletResponse response, @RequestBody Credentials credentials) throws RestException {
        UsernamePasswordToken authToken = new UsernamePasswordToken(credentials.getUsername(), credentials.getPassword(), true);
        try {
            logger.info("Logging user: " + credentials.getUsername());

            SecurityUtils.getSubject().login(authToken);
            User user = userService.validateUserLogin(credentials);
            UserWrapper userWrapper = new UserWrapper(user);
            sessionUtil.setCurrentUser(request, userWrapper);
            return userWrapper;
        } catch (AuthenticationException exception) {
            logger.info("Not user found with username: " + credentials.getUsername() + " and password: " + credentials.getPassword());
            throw new RestException("Wrong credentials", HttpStatus.UNAUTHORIZED, HttpUtil.generateOriginalUrl(request), credentials);
        } catch (RuntimeException ex) {
            logger.info("Error executing login for user: " + credentials.getUsername() + " with error" + ex.getMessage());
            throw new RestException(ex.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR, HttpUtil.generateOriginalUrl(request), credentials);
        }
    }

    @RequestMapping( value = "/logout", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public boolean logout(HttpServletRequest request, HttpServletResponse response) {
        logger.info("Logging out user: " + sessionUtil.getCurrentUser(request).getUsername());
        SecurityUtils.getSubject().logout();
        sessionUtil.removeCurrentUser(request);
        return true;
    }

    @RequestMapping(value = "/logged", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public UserWrapper isLogged(HttpServletRequest request, HttpServletResponse response) {
        return sessionUtil.getCurrentUser(request);
    }
}
