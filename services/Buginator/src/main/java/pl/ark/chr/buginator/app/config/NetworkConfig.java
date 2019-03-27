package pl.ark.chr.buginator.app.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationContextInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import pl.ark.chr.buginator.commons.util.NetworkUtil;

//import org.springframework.boot.context.embedded.EmbeddedServletContainerInitializedEvent;
//import pl.ark.chr.buginator.util.NetworkUtil;


/**
 * Created by Arek on 2017-05-21.
 */
@Configuration
//public class NetworkConfig implements ApplicationListener<EmbeddedServletContainerInitializedEvent> {
public class NetworkConfig implements ApplicationListener<ApplicationContextInitializedEvent> {

    @Value("${buginator.client.host}")
    private String host;
    @Value("${buginator.client.port}")
    private int port;

    private static final Logger logger = LoggerFactory.getLogger(NetworkConfig.class);

    @Override
    public void onApplicationEvent(ApplicationContextInitializedEvent event) {
        NetworkUtil.setHostIP(host);
        NetworkUtil.setHostPort(port);
    }
}
