package pl.ark.chr.buginator.security.oauth2;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import pl.ark.chr.buginator.repository.auth.UserRepository;

//@Service
//@Primary
public class OAuth2UserDetailsService implements UserDetailsService {

    private UserRepository userRepository;

//    @Autowired
    public OAuth2UserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .map(OAuth2UserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("User does not exist"));
    }
}
