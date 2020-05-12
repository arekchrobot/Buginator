package pl.ark.chr.buginator.app.error;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.ark.chr.buginator.commons.util.DtoMapper;
import pl.ark.chr.buginator.domain.core.UserAgentData;

import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = "spring", unmappedTargetPolicy = IGNORE)
public abstract class UserAgentMapper implements DtoMapper<UserAgentData, UserAgentDTO> {

    @Override
    @Mapping(target = "device", expression = "java(combineDeviceInfo(userAgentData.getDevice(), userAgentData.getDeviceType(), userAgentData.getDeviceArchitecture()))")
    @Mapping(target = "operatingSystem", expression = "java(combineOperatingSystem(userAgentData.getOperatingSystemVendor(), userAgentData.getOperatingSystem(), userAgentData.getOperatingSystemVersion()))")
    @Mapping(target = "browser", expression = "java(combineBrowser(userAgentData.getBrowserVendor(), userAgentData.getBrowser(), userAgentData.getBrowserFullVersion()))")
    public abstract UserAgentDTO toDto(UserAgentData userAgentData);

    protected String combineDeviceInfo(String device, String deviceType, String deviceArchitecture) {
        return (device + " " + deviceType + " " + deviceArchitecture).trim();
    }

    protected String combineOperatingSystem(String operatingSystemVendor, String operatingSystem, String operatingSystemVersion) {
        return (operatingSystemVendor + " " + operatingSystem + " " + operatingSystemVersion).trim();
    }

    protected String combineBrowser(String browserVendor, String browser, String browserFullVersion) {
        return (browserVendor + " " + browser + " " + browserFullVersion).trim();
    }
}
