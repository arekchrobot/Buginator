package pl.ark.chr.buginator.domain;

import net.pieroxy.ua.detection.UserAgentDetectionResult;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Created by Arek on 2016-09-26.
 */
@Entity
@Table(name = "user_agent_data")
@SequenceGenerator(name = "default_gen", sequenceName = "user_agent_data_seq", allocationSize = 1)
public class UserAgentData extends BaseEntity {

    private static final long serialVersionUID = -8089095071313692442L;

    @Column(name = "manufacturer", length = 100)
    private String manufacturer;

    @Column(name = "device", length = 100)
    private String device;

    @Column(name = "device_type", length = 100)
    private String deviceType;

    @Column(name = "device_architecture", length = 100)
    private String deviceArchitecture;

    @Column(name = "operating_system", length = 100)
    private String operatingSystem;

    @Column(name = "operating_system_version", length = 100)
    private String operatingSystemVersion;

    @Column(name = "operating_system_description", length = 100)
    private String operatingSystemDescription;

    @Column(name = "operating_system_vendor", length = 100)
    private String operatingSystemVendor;

    @Column(name = "browser", length = 100)
    private String browser;

    @Column(name = "browser_family", length = 100)
    private String browserFamily;

    @Column(name = "browser_version", length = 100)
    private String browserVersion;

    @Column(name = "browser_full_version", length = 100)
    private String browserFullVersion;

    @Column(name = "browser_vendor", length = 100)
    private String browserVendor;

    @Column(name = "country", length = 100)
    private String country;

    @Column(name = "language", length = 100)
    private String language;

    @Column(name = "unknown_tokens", length = 100)
    private String unknownTokens;

    protected UserAgentData() {

    }

    public UserAgentData(UserAgentDetectionResult uadr) {
        if (uadr != null) {
            this.manufacturer = uadr.getDevice().manufacturer.getLabel();
            this.device = uadr.getDevice().device;
            this.deviceType = uadr.getDevice().deviceType.getLabel();
            this.deviceArchitecture = uadr.getDevice().architecture;

            this.operatingSystem = uadr.getOperatingSystem().family.getLabel();
            this.operatingSystemVersion = uadr.getOperatingSystem().version;
            this.operatingSystemDescription = uadr.getOperatingSystem().description;
            this.operatingSystemVendor = uadr.getOperatingSystem().vendor.getLabel();

            this.browser = uadr.getBrowser().description;
            this.browserFamily = uadr.getBrowser().family.getLabel();
            this.browserFullVersion = uadr.getBrowser().fullVersion;
            this.browserVersion = uadr.getBrowser().version;
            this.browserVendor = uadr.getBrowser().vendor.getLabel();

            this.country = uadr.getLocale().country.getLabel();
            this.language = uadr.getLocale().lang.getLabel();

            this.unknownTokens = uadr.getUnknownTokens();
        }
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getDeviceArchitecture() {
        return deviceArchitecture;
    }

    public void setDeviceArchitecture(String deviceArchitecture) {
        this.deviceArchitecture = deviceArchitecture;
    }

    public String getOperatingSystem() {
        return operatingSystem;
    }

    public void setOperatingSystem(String operatingSystem) {
        this.operatingSystem = operatingSystem;
    }

    public String getOperatingSystemVersion() {
        return operatingSystemVersion;
    }

    public void setOperatingSystemVersion(String operatingSystemVersion) {
        this.operatingSystemVersion = operatingSystemVersion;
    }

    public String getOperatingSystemDescription() {
        return operatingSystemDescription;
    }

    public void setOperatingSystemDescription(String operatingSystemDescription) {
        this.operatingSystemDescription = operatingSystemDescription;
    }

    public String getOperatingSystemVendor() {
        return operatingSystemVendor;
    }

    public void setOperatingSystemVendor(String operatingSystemVendor) {
        this.operatingSystemVendor = operatingSystemVendor;
    }

    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }

    public String getBrowserFamily() {
        return browserFamily;
    }

    public void setBrowserFamily(String browserFamily) {
        this.browserFamily = browserFamily;
    }

    public String getBrowserVersion() {
        return browserVersion;
    }

    public void setBrowserVersion(String browserVersion) {
        this.browserVersion = browserVersion;
    }

    public String getBrowserFullVersion() {
        return browserFullVersion;
    }

    public void setBrowserFullVersion(String browserFullVersion) {
        this.browserFullVersion = browserFullVersion;
    }

    public String getBrowserVendor() {
        return browserVendor;
    }

    public void setBrowserVendor(String browserVendor) {
        this.browserVendor = browserVendor;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getUnknownTokens() {
        return unknownTokens;
    }

    public void setUnknownTokens(String unknownTokens) {
        this.unknownTokens = unknownTokens;
    }
}