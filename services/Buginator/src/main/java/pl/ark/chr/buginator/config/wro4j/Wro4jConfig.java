//package pl.ark.chr.buginator.config.wro4j;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.boot.web.servlet.FilterRegistrationBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.env.Environment;
//import ro.isdc.wro.config.jmx.ConfigConstants;
//import ro.isdc.wro.http.ConfigurableWroFilter;
//import ro.isdc.wro.model.resource.processor.factory.ConfigurableProcessorsFactory;
//
//import java.util.Properties;
//
///**
// * Created by Arek on 2016-09-28.
// */
//@Configuration
//public class Wro4jConfig {
//
//    private static final Logger logger = LoggerFactory.getLogger(Wro4jConfig.class);
//
//    private static final String WRO4J_URL_PATTERNS = "/wro/*";
//
//    private static final String[] OTHER_WRO_PROP = new String[]{
//            ConfigurableProcessorsFactory.PARAM_PRE_PROCESSORS,
//            ConfigurableProcessorsFactory.PARAM_POST_PROCESSORS
//    };
//
//    @Bean
//    public FilterRegistrationBean webResourceOptimizer(Environment env) {
//        FilterRegistrationBean fr = new FilterRegistrationBean();
//        ConfigurableWroFilter filter = new ConfigurableWroFilter();
//        Properties props = buildWroProperties(env);
//        filter.setProperties(props);
//        filter.setWroManagerFactory(new Wro4jCustomXmlModelManagerFactory(props));
//        fr.setFilter(filter);
//        fr.addUrlPatterns(WRO4J_URL_PATTERNS);
//        return fr;
//    }
//
//    private Properties buildWroProperties(Environment env) {
//        Properties props = new Properties();
//        for (ConfigConstants c : ConfigConstants.values()) {
//            addProperty(env, props, c.name());
//        }
//        for (String name : OTHER_WRO_PROP) {
//            addProperty(env, props, name);
//        }
//        logger.debug("WRO4J props {}", props);
//        return props;
//    }
//
//    private void addProperty(Environment env, Properties to, String name) {
//        String value = env.getProperty("wro." + name);
//        if(value != null) {
//            to.put(name, value);
//        }
//    }
//}
