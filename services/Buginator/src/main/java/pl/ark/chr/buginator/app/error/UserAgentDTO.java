package pl.ark.chr.buginator.app.error;

public class UserAgentDTO {

    private final String manufacturer;
    private final String device;
    private final String operatingSystem;
    private final String browser;
    private final String country;
    private final String language;

    private UserAgentDTO(Builder builder) {
        manufacturer = builder.manufacturer;
        device = builder.device;
        operatingSystem = builder.operatingSystem;
        browser = builder.browser;
        country = builder.country;
        language = builder.language;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public String getDevice() {
        return device;
    }

    public String getOperatingSystem() {
        return operatingSystem;
    }

    public String getBrowser() {
        return browser;
    }

    public String getCountry() {
        return country;
    }

    public String getLanguage() {
        return language;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String manufacturer;
        private String device;
        private String operatingSystem;
        private String browser;
        private String country;
        private String language;

        private Builder() {
        }

        public Builder manufacturer(String val) {
            manufacturer = val;
            return this;
        }

        public Builder device(String val) {
            device = val;
            return this;
        }

        public Builder operatingSystem(String val) {
            operatingSystem = val;
            return this;
        }

        public Builder browser(String val) {
            browser = val;
            return this;
        }

        public Builder country(String val) {
            country = val;
            return this;
        }

        public Builder language(String val) {
            language = val;
            return this;
        }

        public UserAgentDTO build() {
            return new UserAgentDTO(this);
        }
    }
}
