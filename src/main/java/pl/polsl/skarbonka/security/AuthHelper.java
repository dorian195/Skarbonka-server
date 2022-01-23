package pl.polsl.skarbonka.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import pl.polsl.skarbonka.model.User;

@Component
public class AuthHelper {
    public User.Role getRoleOfAuthentication(Authentication authentication) {
        SimpleGrantedAuthority authority = (SimpleGrantedAuthority) authentication.getAuthorities().stream().findFirst().orElseThrow(IllegalStateException::new);
        return User.Role.valueOf(authority.getAuthority());
    }

    public boolean isAdmin(Authentication authentication) {
        return getRoleOfAuthentication(authentication).equals(User.Role.ADMIN);
    }
}
