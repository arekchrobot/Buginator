package pl.ark.chr.buginator.gateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class ActuatorSecurityWebFilter implements WebFilter {

    static final String ACTUATOR_URI = "/actuator";
    static final String AUTH_HEADER = "Authorization";

    private final String username;
    private final String password;

    @Autowired
    public ActuatorSecurityWebFilter(@Value("${buginator.actuator.credentials.username}") String username,
                                     @Value("${buginator.actuator.credentials.password}") String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange serverWebExchange, WebFilterChain webFilterChain) {

        ServerHttpRequest httpRequest = serverWebExchange.getRequest();

        if (httpRequest.getPath().pathWithinApplication().value().startsWith(ACTUATOR_URI)) {

            final String authorization = httpRequest.getHeaders().getFirst(AUTH_HEADER);

            if (isBasicAuth(authorization)) {

                final String[] values = getCredentials(authorization);

                if (hasAccessToResource(values)) {
                    return webFilterChain.filter(serverWebExchange);
                } else {
                    unauthorized(serverWebExchange.getResponse());
                    return Mono.empty();
                }
            } else {
                unauthorized(serverWebExchange.getResponse());
                return Mono.empty();
            }
        } else {
            return webFilterChain.filter(serverWebExchange);
        }
    }

    void unauthorized(ServerHttpResponse res) {
        res.getHeaders().add("WWW-Authenticate", "Basic realm=\"Protected\"");
        res.setStatusCode(HttpStatus.UNAUTHORIZED);
    }

    boolean isBasicAuth(String authorization) {
        return authorization != null && authorization.toLowerCase().startsWith("basic");
    }

    private String[] getCredentials(String authorization) {
        String base64Credentials = authorization.substring("Basic".length()).trim();

        byte[] credDecoded = Base64.getDecoder().decode(base64Credentials);
        String credentials = new String(credDecoded, StandardCharsets.UTF_8);
        // credentials = username:password
        return credentials.split(":", 2);
    }

    boolean hasAccessToResource(String[] values) {
        return values[0].equals(username) && values[1].equals(password);
    }
}
