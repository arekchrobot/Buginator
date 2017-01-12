package pl.ark.chr.buginator.config.shiro;

import org.apache.shiro.authc.credential.PasswordMatcher;
import org.apache.shiro.authc.credential.PasswordService;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.authc.AnonymousFilter;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.filter.authz.SslFilter;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Arek on 2016-09-28.
 */
@Configuration
public class ShiroConfig {

    @Bean
    public ShiroFilterFactoryBean shiroFilter() {
        ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
        shiroFilter.setSecurityManager(securityManager());

        Map<String, String> filterChainDefinitionMapping = new HashMap<>();
        filterChainDefinitionMapping.put("/auth/login", "anon");
        filterChainDefinitionMapping.put("/auth/logout", "authc");
        filterChainDefinitionMapping.put("/application/**", "http[GET=read_application,POST=create_application,DELETE=http_method_not_allowed]");
        filterChainDefinitionMapping.put("/**", "anon");
        shiroFilter.setFilterChainDefinitionMap(filterChainDefinitionMapping);

        Map<String, Filter> filters = new HashMap<>();
        filters.put("anon", new AnonymousFilter());
        filters.put("authc", new FormAuthenticationFilter());
        filters.put("ssl", new SslFilter());
        filters.put("http", new HttpMethodRolesAuthorizationFilter());

        shiroFilter.setFilters(filters);

        shiroFilter.setLoginUrl("#/login");

        return shiroFilter;
    }

    @Bean(name = "securityManager")
    public DefaultWebSecurityManager securityManager() {
        final DefaultWebSecurityManager securityManager
                = new DefaultWebSecurityManager();
        securityManager.setRealm(realm());
        securityManager.setCacheManager(cacheManager());
        return securityManager;
    }

    @Bean(name = "cacheManager")
    public CacheManager cacheManager() {
        return new MemoryConstrainedCacheManager();
    }

    @Bean(name = "realm")
    @DependsOn("lifecycleBeanPostProcessor")
    public BuginatorAuthorizingRealm realm() {
        BuginatorAuthorizingRealm realm = new BuginatorAuthorizingRealm();
        realm.setCredentialsMatcher(credentialsMatcher());
        return realm;
    }

    @Bean(name = "credentialsMatcher")
    public PasswordMatcher credentialsMatcher() {
        PasswordMatcher credentialsMatcher = new PasswordMatcher();
        credentialsMatcher.setPasswordService(passwordService());
        return credentialsMatcher;
    }

    @Bean(name = "passwordService")
    public PasswordService passwordService() {
        return new BCryptPasswordService();
    }

    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }
}
