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
 * Created by Arek on 2016-12-02.
 */
public class ClientFilterFactoryTest {

    @Test
    public void testCreateClientFilter__ApplicationAccessBeforeDataModifyException() throws DataAccessException {
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

        ClientFilter sut = ClientFilterFactory
                .createClientFilter(ClientFilterFactory.ClientFilterType.APPLICATION_ACCESS, ClientFilterFactory.ClientFilterType.DATA_MODIFY);

        //when
        Executable codeUnderException = () -> sut.validateAccess(filterData, userApplications);

        //then
        var dataAccessException = assertThrows(DataAccessException.class, codeUnderException,
                "should throw DataAccessException");
        assertThat(dataAccessException.getMessage()).isEqualTo("Attempt to access forbidden resources");
    }

    @Test
    public void testCreateClientFilter__DataModifyException() throws DataAccessException {
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
        userApplication1.setModify(true);

        UserApplication userApplication2 = new UserApplication(new User.Builder().build(), application);
//        UserApplicationId userApplicationId2 = new UserApplicationId();
//        userApplicationId2.setApplication(application);
//        userApplication2.setPk(userApplicationId2);
        userApplication2.setModify(false);


        Set<UserApplication> userApplications = new HashSet<>();
        userApplications.add(userApplication1);
        userApplications.add(userApplication2);

        ClientFilter sut = ClientFilterFactory
                .createClientFilter(ClientFilterFactory.ClientFilterType.APPLICATION_ACCESS, ClientFilterFactory.ClientFilterType.DATA_MODIFY);

        //when
        Executable codeUnderException = () -> sut.validateAccess(filterData, userApplications);

        //then
        var dataAccessException = assertThrows(DataAccessException.class, codeUnderException,
                "should throw DataAccessException");
        assertThat(dataAccessException.getMessage()).isEqualTo("User is not permitted to modify application");
    }
}