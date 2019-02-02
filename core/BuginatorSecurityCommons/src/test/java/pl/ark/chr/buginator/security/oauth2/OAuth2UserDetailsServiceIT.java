package pl.ark.chr.buginator.security.oauth2;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import pl.ark.chr.buginator.domain.auth.Company;
import pl.ark.chr.buginator.domain.auth.Role;
import pl.ark.chr.buginator.domain.auth.User;
import pl.ark.chr.buginator.repository.auth.CompanyRepository;
import pl.ark.chr.buginator.repository.auth.RoleRepository;
import pl.ark.chr.buginator.repository.auth.UserRepository;
import pl.ark.chr.buginator.security.util.TestApplicationContext;
import pl.ark.chr.buginator.security.util.TestObjectCreator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {OAuth2DetailsServiceConfig.class, TestApplicationContext.class})
@Transactional
class OAuth2UserDetailsServiceIT {

    @Autowired
    private OAuth2UserDetailsService oAuth2UserDetailsService;

    @SpyBean
    private UserRepository delegatedMockUserRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private RoleRepository roleRepository;

    @AfterEach
    void tearDown() {
        reset(delegatedMockUserRepository);
    }

    @Test
    @DisplayName("should correctly build UserDetails based on User class returned from database by user email")
    void shouldReturnUserDetailsBasedOnUserEmail() {
        //given
        Role role = roleRepository.findById(Role.MANAGER).get();
        Company company = companyRepository.findById(1L).get();
        User user = TestObjectCreator.createUser(company, role, true);
        delegatedMockUserRepository.save(user);

        //when
        UserDetails userDetails = oAuth2UserDetailsService.loadUserByUsername(user.getEmail());

        //then
        assertThat(userDetails).isInstanceOf(OAuth2UserDetails.class);
        assertThat(userDetails.getAuthorities())
                .isNotEmpty()
                .hasSize(1);
        assertThat(userDetails.getPassword()).isEqualTo(user.getPassword());
        assertThat(userDetails.getUsername()).isEqualTo(user.getEmail());
        assertThat(userDetails.isAccountNonExpired()).isTrue();
        assertThat(userDetails.isAccountNonLocked()).isTrue();
        assertThat(userDetails.isCredentialsNonExpired()).isTrue();
        assertThat(userDetails.isEnabled()).isEqualTo(user.isActive());
        verify(delegatedMockUserRepository, times(1))
                .findByEmailAndActiveTrue(eq(user.getEmail()));
    }

    @Test
    @DisplayName("should throw UsernameNotFoundException when user is not active")
    void shouldThrowExceptionWhenUserIsNotActive() {
        //given
        Role role = Role.getRole(Role.MANAGER);
        Company company = companyRepository.findById(1L).get();
        User user = TestObjectCreator.createUser(company, role, false);
        delegatedMockUserRepository.save(user);

        //when
        Executable codeUnderException = () -> oAuth2UserDetailsService.loadUserByUsername(user.getEmail());

        //then
        var usernameNotFoundException = assertThrows(UsernameNotFoundException.class, codeUnderException,
                "Should throw UsernameNotFoundException exception");
        assertThat(usernameNotFoundException.getMessage()).isEqualTo("User does not exist");
        verify(delegatedMockUserRepository, times(1))
                .findByEmailAndActiveTrue(eq(user.getEmail()));
    }

    @Test
    @DisplayName("should throw UsernameNotFoundException when no user found for given email")
    void shouldThrowExceptionWhenNoUserFoundForGivenEmail() {
        //given
        String userEmail = "notExising@email.com";

        //when
        Executable codeUnderException = () -> oAuth2UserDetailsService.loadUserByUsername(userEmail);

        //then
        var usernameNotFoundException = assertThrows(UsernameNotFoundException.class, codeUnderException,
                "Should throw UsernameNotFoundException exception");
        assertThat(usernameNotFoundException.getMessage()).isEqualTo("User does not exist");
        verify(delegatedMockUserRepository, times(1))
                .findByEmailAndActiveTrue(eq(userEmail));
    }
}