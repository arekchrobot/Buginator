package pl.ark.chr.buginator.security.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import pl.ark.chr.buginator.security.actuator.ActuatorSecurityConfig;

@Configuration
@EnableResourceServer
@ImportAutoConfiguration({RedisConfig.class, ActuatorSecurityConfig.class})
public class RedisResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Autowired
    @Qualifier("redisTokenStore")
    private TokenStore redisTokenStore;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.tokenStore(redisTokenStore)
                .resourceId("resource");
    }
}
