package pl.ark.chr.buginator.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.token.TokenStore;
import pl.ark.chr.buginator.security.redis.RedisConfig;
import pl.ark.chr.buginator.security.redis.RedisResourceServerConfig;


@Configuration
@EnableAuthorizationServer
@ImportAutoConfiguration(classes = RedisResourceServerConfig.class)
public class OAuth2Config extends AuthorizationServerConfigurerAdapter {

    private TokenStore tokenStore;
    private AuthenticationManager authenticationManager;
    private ClientDetailsService clientDetailsService;

    @Autowired
    public OAuth2Config(@Qualifier("redisTokenStore") TokenStore tokenStore,
                        AuthenticationManager authenticationManager, ClientDetailsService oauth2ClientDetailsService) {
        this.tokenStore = tokenStore;
        this.authenticationManager = authenticationManager;
        this.clientDetailsService = oauth2ClientDetailsService;
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.tokenStore(tokenStore).authenticationManager(authenticationManager);
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(clientDetailsService);
    }
}
