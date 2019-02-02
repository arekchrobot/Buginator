package pl.ark.chr.buginator.security.oauth2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import pl.ark.chr.buginator.security.util.TestObjectCreator;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletResponse;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static pl.ark.chr.buginator.security.oauth2.OAuth2ClientFilter.X_FORWARDED_FOR_HEADER;

@ExtendWith(MockitoExtension.class)
class OAuth2ClientFilterTest {

    @InjectMocks
    @Spy
    private OAuth2ClientFilter oAuth2ClientFilter;

    @Mock
    private ClientDetailsService clientDetailsService;

    private FilterChain filterChain;

    private HttpServletResponse servletResponse;

    @BeforeEach
    void setUp() {
        filterChain = new MockFilterChain();
        servletResponse = new MockHttpServletResponse();
    }

    @Test
    @DisplayName("should allow access to resource for authentication from allowed domain for client")
    void shouldCorrectlyValidateAllowedDomain() throws Exception {
        //given
        var mockAuth = Mockito.mock(OAuth2Authentication.class);
        var oAuth2Request = Mockito.mock(OAuth2Request.class);

        var clientId = "123";

        doReturn(oAuth2Request).when(mockAuth).getOAuth2Request();
        doReturn(clientId).when(oAuth2Request).getClientId();

        SecurityContextHolder.getContext().setAuthentication(mockAuth);

        var servletRequest = new MockHttpServletRequest();
        servletRequest.setServerName("test.domain.com");

        var oauth2Client = TestObjectCreator.createOAuth2ClientWithAllowedDomains("test.domain.com,acb.domain.com");

        var oauth2ClientDetails = new OAuth2ClientDetails(oauth2Client, 3600);

        doReturn(oauth2ClientDetails).when(clientDetailsService).loadClientByClientId(eq(clientId));

        //when
        oAuth2ClientFilter.doFilter(servletRequest, servletResponse, filterChain);

        //then
        verify(mockAuth, times(1)).getOAuth2Request();
        verify(oAuth2Request, times(1)).getClientId();
        verify(clientDetailsService, times(1)).loadClientByClientId(eq(clientId));
        verify(oAuth2ClientFilter, times(1)).getRequestDomain(eq(servletRequest));
        verify(oAuth2ClientFilter, never()).getRequestIpAddress(eq(servletRequest));
    }

    @Test
    @DisplayName("should reject access to resource for authentication from not allowed domain for client")
    void shouldThrowErrorWhenRequestingFromExternalDomain() throws Exception {
        //given
        var mockAuth = Mockito.mock(OAuth2Authentication.class);
        var oAuth2Request = Mockito.mock(OAuth2Request.class);

        var clientId = "123";

        doReturn(oAuth2Request).when(mockAuth).getOAuth2Request();
        doReturn(clientId).when(oAuth2Request).getClientId();

        SecurityContextHolder.getContext().setAuthentication(mockAuth);

        var servletRequest = new MockHttpServletRequest();
        servletRequest.setServerName("test.rejected.domain.com");

        var oauth2Client = TestObjectCreator.createOAuth2ClientWithAllowedDomains("test.domain.com,acb.domain.com");

        var oauth2ClientDetails = new OAuth2ClientDetails(oauth2Client, 3600);

        doReturn(oauth2ClientDetails).when(clientDetailsService).loadClientByClientId(eq(clientId));

        //when
        Executable codeUnderException = () -> oAuth2ClientFilter.doFilter(servletRequest, servletResponse, filterChain);

        //then
        var runtimeException = assertThrows(RuntimeException.class, codeUnderException,
                "Should throw RuntimeException for not supported domain");
        assertThat(runtimeException.getMessage()).isEqualTo("Domain not supported for given client");
        verify(mockAuth, times(1)).getOAuth2Request();
        verify(oAuth2Request, times(1)).getClientId();
        verify(clientDetailsService, times(1)).loadClientByClientId(eq(clientId));
        verify(oAuth2ClientFilter, times(1)).getRequestDomain(eq(servletRequest));
        verify(oAuth2ClientFilter, never()).getRequestIpAddress(eq(servletRequest));
    }

    @ParameterizedTest(name = "For domain: \"{0}\" and port: \"{1}\"")
    @MethodSource("getRequestDomainProvider")
    @DisplayName("should correctly build requested domain from ServletRequest")
    void shouldReturnCorrectDomainAndPortFromServlet(String domain, int port, String expected) {
        //given
        var servletRequest = new MockHttpServletRequest();
        servletRequest.setServerName(domain);
        servletRequest.setServerPort(port);

        //when
        String result = oAuth2ClientFilter.getRequestDomain(servletRequest);

        //then
        assertThat(result).isEqualTo(expected);
    }

    private static Stream<Arguments> getRequestDomainProvider() {
        return Stream.of(
                Arguments.of("test.domain.com", 80, "test.domain.com"),
                Arguments.of("test.domain.com", 443, "test.domain.com"),
                Arguments.of("test.domain.com", 567, "test.domain.com:567"),
                Arguments.of("212.11.10.7", 8200, "212.11.10.7:8200")
        );
    }

    @Test
    @DisplayName("should allow access to resource for authentication from allowed ip for client")
    void shouldCorrectlyValidateAllowedIp() throws Exception {
        //given
        var mockAuth = Mockito.mock(OAuth2Authentication.class);
        var oAuth2Request = Mockito.mock(OAuth2Request.class);

        var clientId = "123";
        var remoteIp = "212.10.11.128";

        doReturn(oAuth2Request).when(mockAuth).getOAuth2Request();
        doReturn(clientId).when(oAuth2Request).getClientId();

        SecurityContextHolder.getContext().setAuthentication(mockAuth);

        var servletRequest = new MockHttpServletRequest();
        servletRequest.setRemoteAddr(remoteIp);

        var oauth2Client = TestObjectCreator.createOAuth2ClientWithAllowedIps(remoteIp);

        var oauth2ClientDetails = new OAuth2ClientDetails(oauth2Client, 3600);

        doReturn(oauth2ClientDetails).when(clientDetailsService).loadClientByClientId(eq(clientId));

        //when
        oAuth2ClientFilter.doFilter(servletRequest, servletResponse, filterChain);

        //then
        verify(mockAuth, times(1)).getOAuth2Request();
        verify(oAuth2Request, times(1)).getClientId();
        verify(clientDetailsService, times(1)).loadClientByClientId(eq(clientId));
        verify(oAuth2ClientFilter, never()).getRequestDomain(eq(servletRequest));
        verify(oAuth2ClientFilter, times(1)).getRequestIpAddress(eq(servletRequest));
    }

    @Test
    @DisplayName("should reject access to resource for authentication from not allowed ip for client")
    void shouldThrowErrorWhenRequestingFromExternalIp() throws Exception {
        //given
        var mockAuth = Mockito.mock(OAuth2Authentication.class);
        var oAuth2Request = Mockito.mock(OAuth2Request.class);

        var clientId = "123";
        var remoteIp = "212.10.11.128";
        var requestIp = "210.10.11.128";

        doReturn(oAuth2Request).when(mockAuth).getOAuth2Request();
        doReturn(clientId).when(oAuth2Request).getClientId();

        SecurityContextHolder.getContext().setAuthentication(mockAuth);

        var servletRequest = new MockHttpServletRequest();
        servletRequest.setRemoteAddr(remoteIp);

        var oauth2Client = TestObjectCreator.createOAuth2ClientWithAllowedIps(requestIp);

        var oauth2ClientDetails = new OAuth2ClientDetails(oauth2Client, 3600);

        doReturn(oauth2ClientDetails).when(clientDetailsService).loadClientByClientId(eq(clientId));

        //when
        Executable codeUnderException = () -> oAuth2ClientFilter.doFilter(servletRequest, servletResponse, filterChain);

        //then
        var runtimeException = assertThrows(RuntimeException.class, codeUnderException,
                "Should throw RuntimeException for not supported domain");
        assertThat(runtimeException.getMessage()).isEqualTo("IP not supported for given client");
        verify(mockAuth, times(1)).getOAuth2Request();
        verify(oAuth2Request, times(1)).getClientId();
        verify(clientDetailsService, times(1)).loadClientByClientId(eq(clientId));
        verify(oAuth2ClientFilter, never()).getRequestDomain(eq(servletRequest));
        verify(oAuth2ClientFilter, times(1)).getRequestIpAddress(eq(servletRequest));
    }

    @ParameterizedTest(name = "For remoteIp: \"{0}\" and header: \"{1}\"")
    @MethodSource("getRequestIpAddressProvider")
    @DisplayName("should correctly build requested domain from ServletRequest")
    void shouldReturnCorrectRequestIpFromServlet(String remoteAddress, String header, String expected) {
        //given
        var servletRequest = new MockHttpServletRequest();
        servletRequest.setRemoteAddr(remoteAddress);
        if (header != null) {
            servletRequest.addHeader(X_FORWARDED_FOR_HEADER, header);
        }

        //when
        String result = oAuth2ClientFilter.getRequestIpAddress(servletRequest);

        //then
        assertThat(result).isEqualTo(expected);
    }

    private static Stream<Arguments> getRequestIpAddressProvider() {
        return Stream.of(
                Arguments.of("212.10.11.10", null, "212.10.11.10"),
                Arguments.of("212.10.11.10", "212.10.11.115", "212.10.11.115"),
                Arguments.of(null, "212.10.11.115", "212.10.11.115")
        );
    }
}