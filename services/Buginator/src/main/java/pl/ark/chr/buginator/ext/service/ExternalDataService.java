package pl.ark.chr.buginator.ext.service;

import pl.ark.chr.buginator.data.ExternalData;

/**
 * Created by Arek on 2017-04-01.
 */
public interface ExternalDataService {

    void saveErrorAndNotifyAggregators(String uniqueKey, String token, ExternalData externalData);
}
