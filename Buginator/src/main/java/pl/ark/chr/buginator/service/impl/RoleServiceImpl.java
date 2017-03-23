package pl.ark.chr.buginator.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.ark.chr.buginator.domain.Company;
import pl.ark.chr.buginator.domain.Role;
import pl.ark.chr.buginator.exceptions.ValidationException;
import pl.ark.chr.buginator.repository.RoleRepository;
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
    private MessageSource messageSource;

    @Override
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
    public void delete(Long id, Company company) throws ValidationException {
        Role role = get(id, company);
        checkModifyAccess(role, company);
        roleRepository.delete(id);
    }

    @Override
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
}
