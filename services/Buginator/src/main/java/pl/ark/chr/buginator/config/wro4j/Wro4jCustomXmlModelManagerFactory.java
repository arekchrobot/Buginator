//package pl.ark.chr.buginator.config.wro4j;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import ro.isdc.wro.manager.factory.ConfigurableWroManagerFactory;
//import ro.isdc.wro.model.factory.WroModelFactory;
//import ro.isdc.wro.model.factory.XmlModelFactory;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.Properties;
//
///**
// * Created by Arek on 2016-09-28.
// */
//public class Wro4jCustomXmlModelManagerFactory extends ConfigurableWroManagerFactory {
//
//    private static final Logger logger = LoggerFactory.getLogger(Wro4jCustomXmlModelManagerFactory.class);
//
//    private static final String WRO4J_XML_LOCATION = "/wro.xml";
//
//    private final Properties props;
//
//    public Wro4jCustomXmlModelManagerFactory(Properties props) {
//        this.props = props;
//    }
//
//    @Override
//    protected Properties newConfigProperties() {
//        return props;
//    }
//
//    @Override
//    protected WroModelFactory newModelFactory() {
//        logger.debug("Loading from /wro.xml");
//        return new XmlModelFactory() {
//            @Override
//            protected InputStream getModelResourceAsStream() throws IOException {
//                logger.info("Loading resource {}", WRO4J_XML_LOCATION);
//                final InputStream stream = getClass().getResourceAsStream(
//                        WRO4J_XML_LOCATION);
//
//                if (stream == null) {
//                    throw new IOException("Invalid resource requested: "
//                            + WRO4J_XML_LOCATION);
//                }
//
//                return stream;
//            }
//        };
//    }
//}
