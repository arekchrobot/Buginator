package pl.ark.chr.buginator.domain.filter;

import pl.ark.chr.buginator.domain.Application;

/**
 * Used for security reason. Each class implementing this interface should be filtered to return only
 * this data that match given Application
 */
public interface FilterData {

    Application getApplication();
}
