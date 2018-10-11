package pl.ark.chr.buginator.service;

import pl.ark.chr.buginator.data.ChartData;
import pl.ark.chr.buginator.domain.auth.UserApplication;
import pl.ark.chr.buginator.exceptions.ChartException;
import pl.ark.chr.buginator.exceptions.DataAccessException;

import java.util.Set;

/**
 * Created by Arek on 2016-12-25.
 */
public interface ChartService {

    ChartData generateLastWeekErrorsForApplication(Long appId, Set<UserApplication> userApplications) throws ChartException, DataAccessException;
}
