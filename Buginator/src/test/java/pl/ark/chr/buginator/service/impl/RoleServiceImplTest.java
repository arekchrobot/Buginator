package pl.ark.chr.buginator.service.impl;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.MessageSource;
import pl.ark.chr.buginator.TestObjectCreator;
import pl.ark.chr.buginator.domain.auth.Company;
import pl.ark.chr.buginator.domain.auth.Role;
import pl.ark.chr.buginator.exceptions.ValidationException;
import pl.ark.chr.buginator.repository.auth.RoleRepository;
import pl.ark.chr.buginator.service.RoleService;
import pl.wkr.fluentrule.api.FluentExpectedException;

import java.time.LocalDate;
import java.util.Locale;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

/**
 * Created by Arek on 2017-03-22.
 */
@RunWith(MockitoJUnitRunner.class)
public class RoleServiceImplTest {

    private static final String TEST_MESSAGE_SOURCE_RETURN = "TEST INFO";

    @InjectMocks
    private RoleService sut = new RoleServiceImpl();

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private MessageSource messageSource;

    @Rule
    public FluentExpectedException fluentThrown = FluentExpectedException.none();

    @Before
    public void setUp() throws Exception {
        when(messageSource.getMessage(any(String.class), nullable(Object[].class), any(Locale.class))).thenReturn(TEST_MESSAGE_SOURCE_RETURN);
    }

    @Test
    public void testSave__newRole() throws ValidationException {
        //given
        String companyName = "test company";
        Company company = TestObjectCreator.createCompany(LocalDate.now(), companyName, "", "");

        Role role = new Role();
        role.setName("roleName");

        when(roleRepository.save(any(Role.class))).thenAnswer(invocationOnMock1 -> invocationOnMock1.getArguments()[0]);

        //when
        Role result = sut.save(role, company);

        //then
        assertThat(result.getCompany()).isNotNull();
        assertThat(result.getCompany().getName()).isEqualTo(companyName);
    }

    @Test
    public void testCheckModifyAccess_AccessToCompany() throws ValidationException {
        //given
        String companyName = "test company";
        Company company = TestObjectCreator.createCompany(LocalDate.now(), companyName, "", "");

        Role role = new Role();
        role.setName("roleName");
        role.setCompany(company);
        role.setId(1L);

        when(roleRepository.save(any(Role.class))).thenAnswer(invocationOnMock1 -> invocationOnMock1.getArguments()[0]);

        //when
        Role result = sut.save(role, company);

        //then
        assertThat(result).isNotNull();
    }

    @Test
    public void testCheckModifyAccess_NoAccessToCompany() throws ValidationException {
        //given
        String companyName = "test company";
        Company company = TestObjectCreator.createCompany(LocalDate.now(), companyName, "", "");
        Company company2 = TestObjectCreator.createCompany(LocalDate.now(), "TestCompany2", "", "");
        company2.setId(2L);

        Role role = new Role();
        role.setName("roleName");
        role.setCompany(company);
        role.setId(1L);

//        when(roleRepository.save(any(Role.class))).thenAnswer(invocationOnMock1 -> invocationOnMock1.getArguments()[0]);

        fluentThrown
                .expect(ValidationException.class)
                .hasMessage(TEST_MESSAGE_SOURCE_RETURN);

        //when
        sut.save(role, company2);

        //then
    }

    @Test
    public void testCheckModifyAccess_RoleCompanyIsNull() throws ValidationException {
        //given
        String companyName = "test company";
        Company company = TestObjectCreator.createCompany(LocalDate.now(), companyName, "", "");

        Role role = new Role();
        role.setName("roleName");
        role.setCompany(null);
        role.setId(1L);

//        when(roleRepository.save(any(Role.class))).thenAnswer(invocationOnMock1 -> invocationOnMock1.getArguments()[0]);

        fluentThrown
                .expect(ValidationException.class)
                .hasMessage(TEST_MESSAGE_SOURCE_RETURN);

        //when
        sut.save(role, company);

        //then
    }
}