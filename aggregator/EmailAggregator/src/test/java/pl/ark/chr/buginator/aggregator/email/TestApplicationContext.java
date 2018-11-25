package pl.ark.chr.buginator.aggregator.email;

import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration;
import org.springframework.boot.autoconfigure.jms.JmsAutoConfiguration;
import org.springframework.boot.autoconfigure.jms.artemis.ArtemisAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("pl.ark.chr.buginator.aggregator.email")
@ImportAutoConfiguration(classes = {
        FreeMarkerAutoConfiguration.class,
        ArtemisAutoConfiguration.class,
        JmsAutoConfiguration.class
})
public class TestApplicationContext {

}
