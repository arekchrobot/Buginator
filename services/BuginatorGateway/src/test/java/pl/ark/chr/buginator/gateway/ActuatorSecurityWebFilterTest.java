package pl.ark.chr.buginator.gateway;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.server.WebFilterChain;

import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ActuatorSecurityWebFilterTest {

    private ActuatorSecurityWebFilter actuatorSecurityWebFilter;

    private final String requiredUsername = "actuator";
    private final String requiredPassword = "actuator";

    private WebFilterChain webFilterChain;

    @BeforeEach
    void setUp() {
        actuatorSecurityWebFilter = Mockito.spy(new ActuatorSecurityWebFilter(requiredUsername, requiredPassword));
        webFilterChain = Mockito.spy(WebFilterChain.class);

    }

    @Test
    @DisplayName("should allow access to actuator resource when passing correct authorization header")
    void shouldAllowAccessWhenCorrectlyAuthenticated() {
        //given
        var serverHttpRequest = MockServerHttpRequest
                .method(HttpMethod.GET, ActuatorSecurityWebFilter.ACTUATOR_URI)
                .header(ActuatorSecurityWebFilter.AUTH_HEADER, "Basic " +
                        Base64.getEncoder().encodeToString((requiredUsername + ":" + requiredPassword).getBytes()))
                .build();

        var serverWebExchange = MockServerWebExchange.from(serverHttpRequest);

        //when
        actuatorSecurityWebFilter.filter(serverWebExchange, webFilterChain);

        //then
        verify(actuatorSecurityWebFilter, times(1)).isBasicAuth(anyString());
        verify(actuatorSecurityWebFilter, never()).unauthorized(eq(serverWebExchange.getResponse()));
        verify(actuatorSecurityWebFilter, times(1)).hasAccessToResource(any(String[].class));
        verify(webFilterChain, times(1)).filter(eq(serverWebExchange));
    }

    @Test
    @DisplayName("should allow access to resource if the path does not start with actuator uri")
    void shouldAllowAccessWhenUriIsNotActuator() {
        //given
        var serverHttpRequest = MockServerHttpRequest
                .method(HttpMethod.GET, "/api/test")
                .build();

        var serverWebExchange = MockServerWebExchange.from(serverHttpRequest);

        //when
        actuatorSecurityWebFilter.filter(serverWebExchange, webFilterChain);

        //then
        verify(actuatorSecurityWebFilter, never()).isBasicAuth(anyString());
        verify(actuatorSecurityWebFilter, never()).unauthorized(eq(serverWebExchange.getResponse()));
        verify(actuatorSecurityWebFilter, never()).hasAccessToResource(any(String[].class));
        verify(webFilterChain, times(1)).filter(eq(serverWebExchange));
    }

    @Test
    @DisplayName("should return 401 status when no Basic auth header found")
    void shouldReturnUnauthorizedIfNoBasicAuthHeaderFound() {
        //given
        var serverHttpRequest = MockServerHttpRequest
                .method(HttpMethod.GET, ActuatorSecurityWebFilter.ACTUATOR_URI)
                .build();

        var serverWebExchange = MockServerWebExchange.from(serverHttpRequest);

        //when
        actuatorSecurityWebFilter.filter(serverWebExchange, webFilterChain);

        //then
        assertThat(serverWebExchange.getResponse().getStatusCodeValue()).isEqualTo(401);

        verify(actuatorSecurityWebFilter, times(1)).isBasicAuth(nullable(String.class));
        verify(actuatorSecurityWebFilter, times(1)).unauthorized(eq(serverWebExchange.getResponse()));
        verify(actuatorSecurityWebFilter, never()).hasAccessToResource(any(String[].class));
        verify(webFilterChain, never()).filter(eq(serverWebExchange));
    }

    @Test
    @DisplayName("should return 401 status when wrong credentials passed to basic auth")
    void shouldReturnUnauthorizedWhenWrongCredentialsPassed() {
        //given
        var serverHttpRequest = MockServerHttpRequest
                .method(HttpMethod.GET, ActuatorSecurityWebFilter.ACTUATOR_URI)
                .header(ActuatorSecurityWebFilter.AUTH_HEADER, "Basic " +
                        Base64.getEncoder().encodeToString("wrong:pass".getBytes()))
                .build();

        var serverWebExchange = MockServerWebExchange.from(serverHttpRequest);

        //when
        actuatorSecurityWebFilter.filter(serverWebExchange, webFilterChain);

        //then
        assertThat(serverWebExchange.getResponse().getStatusCodeValue()).isEqualTo(401);

        verify(actuatorSecurityWebFilter, times(1)).isBasicAuth(nullable(String.class));
        verify(actuatorSecurityWebFilter, times(1)).unauthorized(eq(serverWebExchange.getResponse()));
        verify(actuatorSecurityWebFilter, times(1)).hasAccessToResource(any(String[].class));
        verify(webFilterChain, never()).filter(eq(serverWebExchange));
    }

    @Test
    @DisplayName("should correctly validate if given auth header is basic auth ignore case")
    void shouldValidateIfAuthHeaderIsBasic() {
        //given
        var serverHttpRequest = MockServerHttpRequest
                .method(HttpMethod.GET, ActuatorSecurityWebFilter.ACTUATOR_URI)
                .header(ActuatorSecurityWebFilter.AUTH_HEADER, "BaSiC " +
                        Base64.getEncoder().encodeToString("wrong:pass".getBytes()))
                .build();

        //when
        boolean result = actuatorSecurityWebFilter.isBasicAuth(serverHttpRequest.getHeaders()
                .getFirst(ActuatorSecurityWebFilter.AUTH_HEADER));

        //then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("should correctly validate if given auth header is not basic auth ignore case")
    void shouldValidateIfAuthHeaderIsNotBasic() {
        //given
        var serverHttpRequest = MockServerHttpRequest
                .method(HttpMethod.GET, ActuatorSecurityWebFilter.ACTUATOR_URI)
                .header(ActuatorSecurityWebFilter.AUTH_HEADER, "Bearer " +
                        Base64.getEncoder().encodeToString("wrong:pass".getBytes()))
                .build();

        //when
        boolean result = actuatorSecurityWebFilter.isBasicAuth(serverHttpRequest.getHeaders()
                .getFirst(ActuatorSecurityWebFilter.AUTH_HEADER));

        //then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("should correctly validate username and password that has access to resource")
    void shouldCorrectlyValidateUsernameAndPassword() {
        //given
        String[] credentials = {requiredUsername, requiredPassword};

        //when
        boolean result = actuatorSecurityWebFilter.hasAccessToResource(credentials);

        //then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("should correctly validate wrong username in basic auth")
    void shouldCorrectlyValidateWrongUsername() {
        //given
        String[] credentials = {"wrongUser", requiredPassword};

        //when
        boolean result = actuatorSecurityWebFilter.hasAccessToResource(credentials);

        //then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("should correctly validate wrong password in basic auth")
    void shouldCorrectlyValidateWrongPassword() {
        //given
        String[] credentials = {requiredUsername, "WrongPass"};

        //when
        boolean result = actuatorSecurityWebFilter.hasAccessToResource(credentials);

        //then
        assertThat(result).isFalse();
    }
}