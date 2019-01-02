package pl.ark.chr.buginator.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.context.MessageSource;
import pl.ark.chr.buginator.TestObjectCreator;
import pl.ark.chr.buginator.domain.auth.Company;
import pl.ark.chr.buginator.domain.auth.Role;
import pl.ark.chr.buginator.exceptions.ValidationException;
import pl.ark.chr.buginator.repository.auth.RoleRepository;
import pl.ark.chr.buginator.service.RoleService;

import java.time.LocalDate;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

/**
 * Created by Arek on 2017-03-22.
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class RoleServiceImplTest {

    private static final String TEST_MESSAGE_SOURCE_RETURN = "TEST INFO";

    @InjectMocks
    private RoleService sut = new RoleServiceImpl();

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private MessageSource messageSource;

    @BeforeEach
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

        //when
        Executable codeUnderException = () -> sut.save(role, company2);

        //then
        var validationException = assertThrows(ValidationException.class, codeUnderException,
                "should throw ValidationException");
        assertThat(validationException.getMessage()).isEqualTo(TEST_MESSAGE_SOURCE_RETURN);
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

        //when
        Executable codeUnderException = () -> sut.save(role, company);

        //then
        var validationException = assertThrows(ValidationException.class, codeUnderException,
                "should throw ValidationException");
        assertThat(validationException.getMessage()).isEqualTo(TEST_MESSAGE_SOURCE_RETURN);
    }
}