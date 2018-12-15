package pl.ark.chr.buginator.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

@Configuration
@PropertySource({"classpath:redis_${spring.profiles.active:dev}.properties"})
public class RedisConfig {

    private String host;
    private int port;

    @Autowired
    public RedisConfig(@Value("${redis.host:localhost}") String host,
                       @Value("${redis.port:6379}") int port) {
        this.host = host;
        this.port = port;
    }

    @Bean
    @Qualifier("redisTokenStore")
    public TokenStore redisTokenStore() {
        return new RedisTokenStore(jedisConnectionFactory());
    }

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(host, port);
        return new JedisConnectionFactory(redisStandaloneConfiguration);
    }
}
