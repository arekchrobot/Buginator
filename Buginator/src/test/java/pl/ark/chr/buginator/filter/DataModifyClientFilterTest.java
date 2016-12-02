package pl.ark.chr.buginator.filter;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import pl.ark.chr.buginator.domain.Application;
import pl.ark.chr.buginator.domain.UserApplication;
import pl.ark.chr.buginator.domain.UserApplicationId;
import pl.ark.chr.buginator.domain.filter.FilterData;
import pl.ark.chr.buginator.exceptions.DataAccessException;
import pl.wkr.fluentrule.api.FluentExpectedException;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Created by Arek on 2016-12-02.
 */
public class DataModifyClientFilterTest {

    private DataModifyClientFilter sut = new DataModifyClientFilter();

    @Rule
    public FluentExpectedException fluentThrown = FluentExpectedException.none();

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testValidateAccess__AccessGranted() throws DataAccessException {
        //given
        Application application = new Application();
        application.setId(1L);

        Application application2 = new Application();
        application2.setId(2L);

        FilterData filterData = () -> application;

        UserApplication userApplication1 = new UserApplication();
        UserApplicationId userApplicationId1 = new UserApplicationId();
        userApplicationId1.setApplication(application);
        userApplication1.setPk(userApplicationId1);
        userApplication1.setModify(true);

        UserApplication userApplication2 = new UserApplication();
        UserApplicationId userApplicationId2 = new UserApplicationId();
        userApplicationId2.setApplication(application2);
        userApplication2.setPk(userApplicationId2);
        userApplication2.setModify(false);

        Set<UserApplication> userApplications = new HashSet<>();
        userApplications.add(userApplication1);
        userApplications.add(userApplication2);

        //when
        sut.validate(filterData, userApplications);

        //then
    }

    @Test
    public void testValidateAccess__NoApplicationId() throws DataAccessException {
        //given
        Application application = new Application();
        application.setId(1L);

        Application application2 = new Application();
        application2.setId(2L);

        FilterData filterData = () -> application;

        UserApplication userApplication1 = new UserApplication();
        UserApplicationId userApplicationId1 = new UserApplicationId();
        userApplicationId1.setApplication(application2);
        userApplication1.setPk(userApplicationId1);


        Set<UserApplication> userApplications = new HashSet<>();
        userApplications.add(userApplication1);

        fluentThrown
                .expect(DataAccessException.class)
                .hasMessage("User is not permitted to modify application");

        //when
        sut.validate(filterData, userApplications);

        //then
    }

    @Test
    public void testValidateAccess__ModifyAccessNotPermitted() throws DataAccessException {
        //given
        Application application = new Application();
        application.setId(1L);

        Application application2 = new Application();
        application2.setId(2L);

        FilterData filterData = () -> application;

        UserApplication userApplication1 = new UserApplication();
        UserApplicationId userApplicationId1 = new UserApplicationId();
        userApplicationId1.setApplication(application);
        userApplication1.setPk(userApplicationId1);
        userApplication1.setModify(false);

        UserApplication userApplication2 = new UserApplication();
        UserApplicationId userApplicationId2 = new UserApplicationId();
        userApplicationId2.setApplication(application2);
        userApplication2.setPk(userApplicationId2);
        userApplication1.setModify(false);

        Set<UserApplication> userApplications = new HashSet<>();
        userApplications.add(userApplication1);
        userApplications.add(userApplication2);

        fluentThrown
                .expect(DataAccessException.class)
                .hasMessage("User is not permitted to modify application");

        //when
        sut.validate(filterData, userApplications);

        //then
    }
}