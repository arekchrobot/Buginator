package pl.ark.chr.buginator.auth.register;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.ark.chr.buginator.commons.exceptions.DuplicateException;
import pl.ark.chr.buginator.domain.auth.Company;
import pl.ark.chr.buginator.domain.auth.PaymentOption;
import pl.ark.chr.buginator.domain.auth.Role;
import pl.ark.chr.buginator.domain.auth.User;
import pl.ark.chr.buginator.repository.auth.CompanyRepository;
import pl.ark.chr.buginator.repository.auth.PaymentOptionRepository;
import pl.ark.chr.buginator.repository.auth.UserRepository;

@Service
class RegisterService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegisterService.class);

    private CompanyRepository companyRepository;
    private UserRepository userRepository;
    private PaymentOptionRepository paymentOptionRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public RegisterService(CompanyRepository companyRepository, UserRepository userRepository,
                           PaymentOptionRepository paymentOptionRepository, PasswordEncoder passwordEncoder) {
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
        this.paymentOptionRepository = paymentOptionRepository;
        this.passwordEncoder = passwordEncoder;
    }

    void registerCompanyAndUser(RegisterDTO registerDTO) {
        validateDuplicateCompany(registerDTO.getCompanyName());
        validateDuplicateEmail(registerDTO.getUserEmail());

        Company company = createAndSaveCompany(registerDTO);

        User user = createAndSaveUser(registerDTO, company);

        //TODO: Send registerEmail
    }

    User createAndSaveUser(RegisterDTO registerDTO, Company company) {
        var managerRole = Role.getRole(Role.MANAGER);

        var user = User.builder()
                .active(true)
                .name(usernameIsBlank(registerDTO.getUserName()) ? registerDTO.getUserEmail() : registerDTO.getUserName())
                .email(registerDTO.getUserEmail())
                .role(managerRole)
                .company(company)
                .password(passwordEncoder.encode(registerDTO.getUserPassword()))
                .build();

        return userRepository.save(user);
    }

    private boolean usernameIsBlank(String username) {
        return username == null || username.isBlank();
    }

    Company createAndSaveCompany(RegisterDTO registerDTO) {
        var paymentOption = paymentOptionRepository.findById(PaymentOption.TRIAL)
                .orElse(PaymentOption.getPaymentOption(PaymentOption.TRIAL));

        var company = new Company(registerDTO.getCompanyName(), paymentOption);
        company.setAddress(registerDTO.getCompanyAddress());

        return companyRepository.save(company);
    }

    private void validateDuplicateEmail(String userEmail) {
        userRepository.findByEmail(userEmail.toLowerCase())
                .ifPresent(user -> {
                    LOGGER.warn("Attempt to create duplicate user: " + userEmail);
                    throw new DuplicateException("user.duplicate");
                });
    }

    private void validateDuplicateCompany(String companyName) {
        companyRepository.findByName(companyName)
                .ifPresent(company -> {
                    LOGGER.warn("Attempt to create duplicate company: " + companyName);
                    throw new DuplicateException("company.duplicate");
                });
    }
}
