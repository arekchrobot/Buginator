package pl.ark.chr.buginator.security.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

@Configuration
@EnableResourceServer
@ImportAutoConfiguration(RedisConfig.class)
public class RedisResourceServerConfig extends ResourceServerConfigurerAdapter {

    private TokenStore redisTokenStore;

    @Autowired
    public RedisResourceServerConfig(@Qualifier("redisTokenStore") TokenStore redisTokenStore) {
        this.redisTokenStore = redisTokenStore;
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.tokenStore(redisTokenStore)
                .resourceId("resource");
    }
}
