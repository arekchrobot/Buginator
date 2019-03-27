package pl.ark.chr.buginator.app.core.security.filter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Arek on 2016-12-02.
 */
public class ClientFilterFactory {

    public enum ClientFilterType {
        APPLICATION_ACCESS,
        DATA_MODIFY
    }

    public static ClientFilter createClientFilter(ClientFilterType... filterFactoryArray) {
        return createClientFilter(Arrays.asList(filterFactoryArray));
    }

    public static ClientFilter createClientFilter(List<ClientFilterType> filterFactories) {
        Collections.reverse(filterFactories);

        return createClientFilterInternal(filterFactories);
    }

    private static ClientFilter createClientFilterInternal(List<ClientFilterType> filterFactories) {
        ClientFilter clientFilter = new EmptyClientFilter();

        for (ClientFilterType clientFilterType : filterFactories) {
            switch(clientFilterType) {
                case APPLICATION_ACCESS:
                    clientFilter = assignClientFilter(clientFilter, new ApplicationAccessClientFilter());
                    break;
                case DATA_MODIFY:
                    clientFilter = assignClientFilter(clientFilter, new DataModifyClientFilter());
                    break;
            }
        }

        return clientFilter;
    }

    private static ClientFilter assignClientFilter(ClientFilter clientFilter, ClientFilter newClientFilter) {
        newClientFilter.setNextClientFilter(clientFilter);
        return newClientFilter;
    }
}
