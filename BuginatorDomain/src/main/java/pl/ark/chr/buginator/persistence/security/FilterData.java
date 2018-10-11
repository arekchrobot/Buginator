package pl.ark.chr.buginator.persistence.security;

import pl.ark.chr.buginator.domain.core.Application;

/**
 * Used for security reason. Each class implementing this interface should be filtered to return only
 * this data that match given Application
 */
public interface FilterData {

    Application getApplication();
}
