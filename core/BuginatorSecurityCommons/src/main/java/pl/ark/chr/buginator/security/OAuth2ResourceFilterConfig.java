package pl.ark.chr.buginator.security;

import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.ClientDetailsService;

@Configuration
@ImportAutoConfiguration(OAuth2DetailsServiceConfig.class)
public class OAuth2ResourceFilterConfig {

    @Bean
    public FilterRegistrationBean oauth2ClientFilter(ClientDetailsService oauth2ClientDetailsService) {
        FilterRegistrationBean bean = new FilterRegistrationBean<>(new OAuth2ClientFilter(oauth2ClientDetailsService));
        bean.setOrder(2);
        return bean;
    }
}
