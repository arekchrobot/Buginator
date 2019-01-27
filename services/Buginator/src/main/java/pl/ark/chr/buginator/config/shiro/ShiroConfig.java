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
        filterChainDefinitionMapping.put("/error/**", "http[GET=read_application,POST=read_application,DELETE=http_method_not_allowed]");
        filterChainDefinitionMapping.put("/manageUser/**", "http[GET=app_manage_users,POST=app_manage_users,PUT=app_manage_users,DELETE=app_manage_users]");
        filterChainDefinitionMapping.put("/aggregator/**", "http[GET=app_show_notification,POST=app_modify_notification,PUT=app_modify_notification,DELETE=app_modify_notification]");
        filterChainDefinitionMapping.put("/notification/**", "authc");
        filterChainDefinitionMapping.put("/chart/**", "authc");
        filterChainDefinitionMapping.put("/role/**", "http[GET=manage_role,POST=manage_role,DELETE=manage_role]");
        filterChainDefinitionMapping.put("/user/**", "http[GET=manage_user;manage_role,POST=manage_user;manage_role,PUT=manage_user;manage_role]");
        filterChainDefinitionMapping.put("/ext/notify/**", "anon");
        filterChainDefinitionMapping.put("/aggregatorLog/**", "http[GET=app_show_notification_log]");
        shiroFilter.setFilterChainDefinitionMap(filterChainDefinitionMapping);

        Map<String, Filter> filters = new HashMap<>();
        filters.put("anon", new AnonymousFilter());
        filters.put("authc", new FormAuthenticationFilter());
        filters.put("ssl", new SslFilter());
        filters.put("http", new HttpMethodRolesAuthorizationFilter());

        shiroFilter.setFilters(filters);

        shiroFilter.setLoginUrl("/#/login");

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

    @Bean(name = "shiroCacheManager")
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