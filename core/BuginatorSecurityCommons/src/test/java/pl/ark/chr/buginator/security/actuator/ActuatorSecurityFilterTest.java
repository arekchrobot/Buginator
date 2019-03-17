package pl.ark.chr.buginator.security.actuator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletResponse;
import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ActuatorSecurityFilterTest {

    private ActuatorSecurityFilter actuatorSecurityFilter;

    private final String requiredUsername = "actuator";
    private final String requiredPassword = "actuator";

    private FilterChain filterChain;
    private HttpServletResponse servletResponse;

    @BeforeEach
    void setUp() {
        actuatorSecurityFilter = Mockito.spy(new ActuatorSecurityFilter(requiredUsername, requiredPassword));
        filterChain = Mockito.spy(new MockFilterChain());
        servletResponse = new MockHttpServletResponse();
    }

    @Test
    @DisplayName("should allow access to actuator resource when passing correct authorization header")
    void shouldAllowAccessWhenCorrectlyAuthenticated() throws Exception {
        //given
        var servletRequest = new MockHttpServletRequest();
        servletRequest.addHeader(ActuatorSecurityFilter.AUTH_HEADER, "Basic " +
                Base64.getEncoder().encodeToString((requiredUsername + ":" + requiredPassword).getBytes()));
        servletRequest.setRequestURI(ActuatorSecurityFilter.ACTUATOR_URI);

        //when
        actuatorSecurityFilter.doFilter(servletRequest, servletResponse, filterChain);

        //then
        verify(actuatorSecurityFilter, times(1)).isBasicAuth(anyString());
        verify(actuatorSecurityFilter, never()).unauthorized(eq(servletResponse));
        verify(actuatorSecurityFilter, times(1)).hasAccessToResource(any(String[].class));
        verify(filterChain, times(1)).doFilter(eq(servletRequest), eq(servletResponse));
    }

    @Test
    @DisplayName("should allow access to resource if the path does not start with actuator uri")
    void shouldAllowAccessWhenUriIsNotActuator() throws Exception {
        //given
        var servletRequest = new MockHttpServletRequest();
        servletRequest.setRequestURI("/api/test");

        //when
        actuatorSecurityFilter.doFilter(servletRequest, servletResponse, filterChain);

        //then
        verify(actuatorSecurityFilter, never()).isBasicAuth(anyString());
        verify(actuatorSecurityFilter, never()).unauthorized(eq(servletResponse));
        verify(actuatorSecurityFilter, never()).hasAccessToResource(any(String[].class));
        verify(filterChain, times(1)).doFilter(eq(servletRequest), eq(servletResponse));
    }

    @Test
    @DisplayName("should return 401 status when no Basic auth header found")
    void shouldReturnUnauthorizedIfNoBasicAuthHeaderFound() throws Exception {
        //given
        var servletRequest = new MockHttpServletRequest();
        servletRequest.setRequestURI(ActuatorSecurityFilter.ACTUATOR_URI);

        //when
        actuatorSecurityFilter.doFilter(servletRequest, servletResponse, filterChain);

        //then
        assertThat(servletResponse.getStatus()).isEqualTo(401);
        assertThat(servletResponse.isCommitted()).isTrue();

        verify(actuatorSecurityFilter, times(1)).isBasicAuth(nullable(String.class));
        verify(actuatorSecurityFilter, times(1)).unauthorized(eq(servletResponse));
        verify(actuatorSecurityFilter, never()).hasAccessToResource(any(String[].class));
        verify(filterChain, never()).doFilter(eq(servletRequest), eq(servletResponse));
    }

    @Test
    @DisplayName("should return 401 status when wrong credentials passed to basic auth")
    void shouldReturnUnauthorizedWhenWrongCredentialsPassed() throws Exception {
        //given
        var servletRequest = new MockHttpServletRequest();
        servletRequest.addHeader(ActuatorSecurityFilter.AUTH_HEADER, "Basic " +
                Base64.getEncoder().encodeToString("wrong:pass".getBytes()));
        servletRequest.setRequestURI(ActuatorSecurityFilter.ACTUATOR_URI);

        //when
        actuatorSecurityFilter.doFilter(servletRequest, servletResponse, filterChain);

        //then
        assertThat(servletResponse.getStatus()).isEqualTo(401);
        assertThat(servletResponse.isCommitted()).isTrue();

        verify(actuatorSecurityFilter, times(1)).isBasicAuth(nullable(String.class));
        verify(actuatorSecurityFilter, times(1)).unauthorized(eq(servletResponse));
        verify(actuatorSecurityFilter, times(1)).hasAccessToResource(any(String[].class));
        verify(filterChain, never()).doFilter(eq(servletRequest), eq(servletResponse));
    }

    @Test
    @DisplayName("should correctly validate if given auth header is basic auth ignore case")
    void shouldValidateIfAuthHeaderIsBasic() {
        //given
        var servletRequest = new MockHttpServletRequest();
        servletRequest.addHeader(ActuatorSecurityFilter.AUTH_HEADER, "BaSiC " +
                Base64.getEncoder().encodeToString("wrong:pass".getBytes()));

        //when
        boolean result = actuatorSecurityFilter.isBasicAuth(servletRequest.getHeader(ActuatorSecurityFilter.AUTH_HEADER));

        //then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("should correctly validate if given auth header is not basic auth ignore case")
    void shouldValidateIfAuthHeaderIsNotBasic() {
        //given
        var servletRequest = new MockHttpServletRequest();
        servletRequest.addHeader(ActuatorSecurityFilter.AUTH_HEADER, "Bearer " +
                Base64.getEncoder().encodeToString("wrong:pass".getBytes()));

        //when
        boolean result = actuatorSecurityFilter.isBasicAuth(servletRequest.getHeader(ActuatorSecurityFilter.AUTH_HEADER));

        //then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("should correctly validate username and password that has access to resource")
    void shouldCorrectlyValidateUsernameAndPassword() {
        //given
        String[] credentials = {requiredUsername, requiredPassword};

        //when
        boolean result = actuatorSecurityFilter.hasAccessToResource(credentials);

        //then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("should correctly validate wrong username in basic auth")
    void shouldCorrectlyValidateWrongUsername() {
        //given
        String[] credentials = {"wrongUser", requiredPassword};

        //when
        boolean result = actuatorSecurityFilter.hasAccessToResource(credentials);

        //then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("should correctly validate wrong password in basic auth")
    void shouldCorrectlyValidateWrongPassword() {
        //given
        String[] credentials = {requiredUsername, "WrongPass"};

        //when
        boolean result = actuatorSecurityFilter.hasAccessToResource(credentials);

        //then
        assertThat(result).isFalse();
    }
}