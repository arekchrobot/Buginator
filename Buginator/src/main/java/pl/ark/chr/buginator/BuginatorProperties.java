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
    private int schedulerThreads;
    private int errorMonthsOldToRemove;

    public int getBcryptStrength() {
        return bcryptStrength;
    }

    public void setBcryptStrength(int bcryptStrength) {
        this.bcryptStrength = bcryptStrength;
    }

    public int getSchedulerThreads() {
        return schedulerThreads;
    }

    public void setSchedulerThreads(int schedulerThreads) {
        this.schedulerThreads = schedulerThreads;
    }

    public int getErrorMonthsOldToRemove() {
        return errorMonthsOldToRemove;
    }

    public void setErrorMonthsOldToRemove(int errorMonthsOldToRemove) {
        this.errorMonthsOldToRemove = errorMonthsOldToRemove;
    }
}
