package pl.ark.chr.buginator.app.core.security.filter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import pl.ark.chr.buginator.app.application.UserApplicationDTO;
import pl.ark.chr.buginator.domain.auth.*;
import pl.ark.chr.buginator.domain.core.Application;
import pl.ark.chr.buginator.persistence.security.FilterData;
import pl.ark.chr.buginator.app.exceptions.DataAccessException;

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

        UserApplicationDTO userApplication1 = UserApplicationDTO.builder()
                .id(application2.getId())
                .modify(false)
                .build();

        UserApplicationDTO userApplication2 = UserApplicationDTO.builder()
                .id(application.getId())
                .modify(false)
                .build();

        Set<UserApplicationDTO> userApplications = new HashSet<>();
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

        UserApplicationDTO userApplication1 = UserApplicationDTO.builder()
                .id(application2.getId())
                .modify(false)
                .build();

        Set<UserApplicationDTO> userApplications = new HashSet<>();
        userApplications.add(userApplication1);

        //when
        Executable codeUnderException = () -> sut.validate(filterData, userApplications);

        //then
        var dataAccessException = assertThrows(DataAccessException.class, codeUnderException,
                "should throw DataAccessException.class");
        assertThat(dataAccessException.getMessage()).isEqualTo("Attempt to access forbidden resources");
    }

}