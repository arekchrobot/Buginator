package pl.ark.chr.buginator.service;

import pl.ark.chr.buginator.domain.Company;
import pl.ark.chr.buginator.domain.Permission;
import pl.ark.chr.buginator.domain.Role;
import pl.ark.chr.buginator.exceptions.ValidationException;

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
