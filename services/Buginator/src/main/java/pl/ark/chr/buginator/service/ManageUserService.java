package pl.ark.chr.buginator.service;

import pl.ark.chr.buginator.data.ManageUserData;
import pl.ark.chr.buginator.data.UserWrapper;
import pl.ark.chr.buginator.domain.auth.UserApplication;
import pl.ark.chr.buginator.app.exceptions.DataAccessException;

import java.util.List;
import java.util.Set;

/**
 * Created by Arek on 2017-02-04.
 */
public interface ManageUserService {

    List<ManageUserData> getAllApplicationUsers(Long appId, Set<UserApplication> userApplications) throws DataAccessException;
    List<ManageUserData> getAllUsersNotInApplication(Long appId, UserWrapper userWrapper) throws DataAccessException;
    void addUsersToApplication(List<ManageUserData> usersToAdd, Long appId, Set<UserApplication> userApplications) throws DataAccessException;
    void removeUsersFromApplication(List<ManageUserData> usersToRemove, Long appId, Set<UserApplication> userApplications) throws DataAccessException;
    void changeAccessToAppForUser(ManageUserData userData, Long appId, Set<UserApplication> userApplications) throws DataAccessException;
}
