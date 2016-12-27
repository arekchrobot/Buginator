package pl.ark.chr.buginator;

import pl.ark.chr.buginator.data.UserWrapper;
import pl.ark.chr.buginator.domain.*;
import pl.ark.chr.buginator.domain.Error;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Arek on 2016-12-26.
 */
public class TestObjectCreator {

    public static Role createRole(String name) {
        Role role = new Role();
        role.setId(1L);
        if (name != null) {
            role.setName(name);
        } else {
            role.setName("Test role");
        }

        return role;
    }

    public static User createUser(String email, Role role, Company company, String password) {
        User user = new User();
        user.setEmail(email);
        user.setId(1L);
        user.setRole(role);
        user.setCompany(company);
        if(password != null) {
            user.setPassword(password);
        }

        return user;
    }

    public static Company createCompany(LocalDate expiryDate, String name, String token, String uniqueId) {
        Company company = new Company();
        company.setId(1L);
        company.setExpiryDate(expiryDate);
        if(name != null) {
            company.setName(name);
        }
        if(token != null) {
            company.setToken(token);
        }
        if(uniqueId != null) {
            company.setUniqueKey(uniqueId);
        }

        return company;
    }

    public static Application createApplication(Company company, String name) {
        Application application = new Application();
        application.setName(name != null ? name : "Test Application");
        application.setId(1L);
        application.setCompany(company);

        return application;
    }

    public static UserApplication createUserApplication(User user, Application application, boolean modify) {
        UserApplication userApplication = new UserApplication();
        userApplication.setApplication(application);
        userApplication.setUser(user);
        userApplication.setModify(modify);

        return userApplication;
    }

    public static UserWrapper createUserWrapper(User user) {
        return new UserWrapper(user);
    }

    public static List<Error> generateErrorListForLastWeekForApplication(Application application) {
        LocalDate now = LocalDate.now();

        LocalDate minusSevenDays = now.minusDays(7);
        LocalDate minusFourDays = now.minusDays(4);
        LocalDate minusOneDays = now.minusDays(1);

        Error error1 = new Error();
        error1.setApplication(application);
        error1.setLastOccurrence(minusSevenDays);

        Error error2 = new Error();
        error2.setApplication(application);
        error2.setLastOccurrence(minusSevenDays);

        Error error3 = new Error();
        error3.setApplication(application);
        error3.setLastOccurrence(minusSevenDays);

        Error error4= new Error();
        error4.setApplication(application);
        error4.setLastOccurrence(minusFourDays);

        Error error5 = new Error();
        error5.setApplication(application);
        error5.setLastOccurrence(minusFourDays);

        Error error6 = new Error();
        error6.setApplication(application);
        error6.setLastOccurrence(minusOneDays);

        Error error7 = new Error();
        error7.setApplication(application);
        error7.setLastOccurrence(now);

        Error error8 = new Error();
        error8.setApplication(application);
        error8.setLastOccurrence(now);

        Error error9 = new Error();
        error9.setApplication(application);
        error9.setLastOccurrence(now);

        return Arrays.asList(error1,error2,error3,error4,error5,error6,error7,error8,error9);
    }
}
