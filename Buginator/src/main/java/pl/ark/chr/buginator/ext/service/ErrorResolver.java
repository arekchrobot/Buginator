package pl.ark.chr.buginator.ext.service;

import pl.ark.chr.buginator.data.ExternalData;
import pl.ark.chr.buginator.domain.core.Application;
import pl.ark.chr.buginator.domain.core.Error;

/**
 * Created by Arek on 2017-04-03.
 */
public interface ErrorResolver {

    Error resolveError(ExternalData externalData, Application application);
}
