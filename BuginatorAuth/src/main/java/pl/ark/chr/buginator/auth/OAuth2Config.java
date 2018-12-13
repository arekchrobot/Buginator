package pl.ark.chr.buginator.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import pl.ark.chr.buginator.redis.RedisConfig;

import java.util.Collections;

@Configuration
@EnableAuthorizationServer
@ImportAutoConfiguration(classes = RedisConfig.class)
public class OAuth2Config extends AuthorizationServerConfigurerAdapter {

    private TokenStore tokenStore;
    private AuthenticationManager authenticationManager;
    private int expiration;
    private TokenEnhancer tokenEnhancer;
    private UserDetailsService userDetailsService;
    private ClientDetailsService clientDetailsService;

    @Autowired
    public OAuth2Config(@Qualifier("redisTokenStore") TokenStore tokenStore, AuthenticationManager authenticationManager,
                        @Value("${oauth.expiration:3600}") int expiration, TokenEnhancer tokenEnhancer,
                        UserDetailsService userDetailsService, ClientDetailsService clientDetailsService) {
        this.tokenStore = tokenStore;
        this.authenticationManager = authenticationManager;
        this.expiration = expiration;
        this.tokenEnhancer = tokenEnhancer;
        this.userDetailsService = userDetailsService;
        this.clientDetailsService = clientDetailsService;
    }

    @Override
    public void configure(final AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        oauthServer.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()");
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        final TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Collections.singletonList(tokenEnhancer));
        endpoints.tokenStore(tokenStore)
                .tokenEnhancer(tokenEnhancerChain)
                .authenticationManager(authenticationManager)
                .userDetailsService(userDetailsService);
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(clientDetailsService);
//        clients.inMemory().withClient("fooClientIdPassword").secret("{noop}secret").accessTokenValiditySeconds(expiration)
//                .scopes("read", "write").authorizedGrantTypes("password", "authorization_code").resourceIds("resource");
    }

}
