package sg.edu.ntu.nutrimate.security;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    CustomUserDetailsService userDetailsService;

    @Autowired
    UserSecurityServiceImpl userSecurityService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        System.out.print("Authenticating");

        String username = authentication.getName();
        String providePassword = (String) authentication.getCredentials();

        try {
            UserDetails authenticatedUser = userDetailsService.loadUserByUsername(username);

            if (!userSecurityService.checkIfValidPassword(authenticatedUser.getPassword(), providePassword)) {
                throw new BadCredentialsException("Invalid password.");
            }

            Collection<? extends GrantedAuthority> authorities = authenticatedUser.getAuthorities();
            return new UsernamePasswordAuthenticationToken(authenticatedUser, providePassword, authorities);

        } catch (UsernameNotFoundException ex) {
            throw new BadCredentialsException("Username not found.");
        }        
    }

    @Override
    public boolean supports(Class<?> arg0) {
        return true;
    }

}
