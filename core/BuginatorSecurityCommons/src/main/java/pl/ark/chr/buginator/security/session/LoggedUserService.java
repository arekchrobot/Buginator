package pl.ark.chr.buginator.security.session;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.ark.chr.buginator.commons.dto.LoggedUserDTO;
import pl.ark.chr.buginator.security.oauth2.OAuth2UserDetails;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class LoggedUserService {

    public LoggedUserDTO getCurrentUser() {
        OAuth2UserDetails userDetails = (OAuth2UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Set<String> permissions = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());
        return new LoggedUserDTO(userDetails.getName(), userDetails.getUsername(), permissions);
    }
}
