package pl.ark.chr.buginator.core.security.filter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import pl.ark.chr.buginator.domain.auth.*;
import pl.ark.chr.buginator.domain.core.Application;
import pl.ark.chr.buginator.persistence.security.FilterData;
import pl.ark.chr.buginator.exceptions.DataAccessException;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Created by Arek on 2016-12-01.
 */
public class ApplicationAccessClientFilterTest {

    private ApplicationAccessClientFilter sut = new ApplicationAccessClientFilter();

    @Test
    public void testValidateAccess__AccessGranted() throws DataAccessException {
        //given
        Application application = new Application("", new Company("", new PaymentOption()));
        application.setId(1L);

        Application application2 = new Application("", new Company("", new PaymentOption()));
        application2.setId(2L);

        FilterData filterData = () -> application;

        UserApplication userApplication1 = new UserApplication(new User.Builder().build(), application);
//        UserApplicationId userApplicationId1 = new UserApplicationId();
//        userApplicationId1.setApplication(application);
//        userApplication1.setPk(userApplicationId1);

        UserApplication userApplication2 = new UserApplication(new User.Builder().build(), application2);
//        UserApplicationId userApplicationId2 = new UserApplicationId();
//        userApplicationId2.setApplication(application2);
//        userApplication2.setPk(userApplicationId2);

        Set<UserApplication> userApplications = new HashSet<>();
        userApplications.add(userApplication1);
        userApplications.add(userApplication2);

        //when
        sut.validate(filterData, userApplications);

        //then
    }

    @Test
    public void testValidateAccess__DataAccessExceptionThrown() throws DataAccessException {
        //given
        Application application = new Application("", new Company("", new PaymentOption()));
        application.setId(1L);

        Application application2 = new Application("", new Company("", new PaymentOption()));
        application2.setId(2L);

        FilterData filterData = () -> application;

        UserApplication userApplication1 = new UserApplication(new User.Builder().build(), application2);
//        UserApplicationId userApplicationId1 = new UserApplicationId();
//        userApplicationId1.setApplication(application2);
//        userApplication1.setPk(userApplicationId1);


        Set<UserApplication> userApplications = new HashSet<>();
        userApplications.add(userApplication1);

        //when
        Executable codeUnderException = () -> sut.validate(filterData, userApplications);

        //then
        var dataAccessException = assertThrows(DataAccessException.class, codeUnderException,
                "should throw DataAccessException.class");
        assertThat(dataAccessException.getMessage()).isEqualTo("Attempt to access forbidden resources");
    }

}