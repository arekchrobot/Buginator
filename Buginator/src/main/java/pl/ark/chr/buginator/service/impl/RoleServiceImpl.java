package pl.ark.chr.buginator.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.ark.chr.buginator.domain.Company;
import pl.ark.chr.buginator.domain.Permission;
import pl.ark.chr.buginator.domain.Role;
import pl.ark.chr.buginator.domain.User;
import pl.ark.chr.buginator.exceptions.ValidationException;
import pl.ark.chr.buginator.repository.PermissionRepository;
import pl.ark.chr.buginator.repository.RoleRepository;
import pl.ark.chr.buginator.repository.UserRepository;
import pl.ark.chr.buginator.service.RoleService;

import java.util.List;
import java.util.Locale;

/**
 * Created by Arek on 2017-03-22.
 */
@Service
@Transactional
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private UserRepository userRepository;

    @Override
    @CacheEvict(value = "roles", key = "#company.id")
    public Role save(Role role, Company company) throws ValidationException {
        if (role.isNew()) {
            role.setCompany(company);
        } else {
            checkAccess(role, company);
            checkModifyAccess(role, company);
        }
        return roleRepository.save(role);
    }

    @Override
    public Role get(Long id, Company company) throws ValidationException {
        Role role = roleRepository.findOne(id);
        checkAccess(role, company);
        return role;
    }

    @Override
    @CacheEvict(value = "roles", key = "#company.id")
    public void delete(Long id, Company company) throws ValidationException {
        Role role = get(id, company);
        checkModifyAccess(role, company);
        checkUsersExists(role);
        roleRepository.delete(id);
    }

    @Override
    @Cacheable("permissions")
    public List<Permission> getAllPermissions() {
        return (List<Permission>) permissionRepository.findAll();
    }

    @Override
    @Cacheable(value = "roles", key = "#company.id")
    public List<Role> getAll(Company company) {
        return roleRepository.findAllByCompany(company);
    }

    private void checkAccess(Role role, Company company) throws ValidationException {
        if (role.getCompany() != null && !role.getCompany().getId().equals(company.getId())) {
            Locale locale = LocaleContextHolder.getLocale();
            throw new ValidationException(messageSource.getMessage("roleException.unauthorizedOperation", null, locale));
        }
    }

    private void checkModifyAccess(Role role, Company company) throws ValidationException {
        if (role.getCompany() == null || !role.getCompany().getId().equals(company.getId())) {
            Locale locale = LocaleContextHolder.getLocale();
            throw new ValidationException(messageSource.getMessage("roleException.unauthorizedOperation", null, locale));
        }
    }

    private void checkUsersExists(Role role) throws ValidationException {
        List<User> roleUsers = userRepository.findByRole(role);
        if(roleUsers != null && !roleUsers.isEmpty()) {
            Locale locale = LocaleContextHolder.getLocale();
            throw new ValidationException(messageSource.getMessage("roleException.usersExists", null, locale));
        }
    }
}
