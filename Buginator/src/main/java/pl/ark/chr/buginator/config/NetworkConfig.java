//package pl.ark.chr.buginator.config;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.boot.context.embedded.EmbeddedServletContainerInitializedEvent;
//import org.springframework.context.ApplicationListener;
//import org.springframework.context.annotation.Configuration;
//import pl.ark.chr.buginator.util.NetworkUtil;
//
//import java.net.InetAddress;
//import java.net.UnknownHostException;
//
///**
// * Created by Arek on 2017-05-21.
// */
//@Configuration
//public class NetworkConfig implements ApplicationListener<EmbeddedServletContainerInitializedEvent> {
//
//    private static final Logger logger = LoggerFactory.getLogger(NetworkConfig.class);
//    @Override
//    public void onApplicationEvent(EmbeddedServletContainerInitializedEvent event) {
//        try {
//            NetworkUtil.setHostIP(InetAddress.getLocalHost().getHostAddress());
//        } catch (UnknownHostException e) {
//            logger.warn("Unable to get host. Will set default.");
//            NetworkUtil.setHostIP("127.0.0.1");
//        }
//        NetworkUtil.setHostPort(event.getEmbeddedServletContainer().getPort());
//    }
//}
