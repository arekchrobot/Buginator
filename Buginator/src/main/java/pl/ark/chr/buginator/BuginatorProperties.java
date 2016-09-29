package pl.ark.chr.buginator;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by Arek on 2016-09-28.
 */
@ConfigurationProperties("buginator")
@Component
public class BuginatorProperties {

    private int bcryptStrength;

    public int getBcryptStrength() {
        return bcryptStrength;
    }

    public void setBcryptStrength(int bcryptStrength) {
        this.bcryptStrength = bcryptStrength;
    }
}
