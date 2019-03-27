package pl.ark.chr.buginator.service;

import pl.ark.chr.buginator.domain.auth.Company;
import pl.ark.chr.buginator.domain.auth.Permission;
import pl.ark.chr.buginator.domain.auth.Role;
import pl.ark.chr.buginator.app.exceptions.ValidationException;

import java.util.List;

/**
 * Created by Arek on 2017-03-22.
 */
public interface RoleService {

    List<Role> getAll(Company company);

    Role save(Role role, Company company) throws ValidationException;

    Role get(Long id, Company company)  throws ValidationException;

    void delete(Long id, Company company)  throws ValidationException;

    List<Permission> getAllPermissions();
}
