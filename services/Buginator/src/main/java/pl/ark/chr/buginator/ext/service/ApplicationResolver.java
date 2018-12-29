package pl.ark.chr.buginator.ext.service;

import pl.ark.chr.buginator.domain.core.Application;

/**
 * Created by Arek on 2017-04-03.
 */
public interface ApplicationResolver {

    Application resolveApplication(String uniqueKey, String token, String applicationName);
}
