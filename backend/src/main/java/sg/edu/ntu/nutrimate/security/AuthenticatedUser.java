package sg.edu.ntu.nutrimate.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class AuthenticatedUser implements UserDetails{

    String ROLE_PREFIX = "ROLE_";

    private String username; 
    private String password;
    private boolean accountNonLocked;
    private String userrole;
    
    public AuthenticatedUser() {
    }

    public AuthenticatedUser(String username, String password, String userrole, boolean accountNonLocked) {
        this.username = username;
        this.password = password;
        this.userrole = userrole;
        this.accountNonLocked = accountNonLocked;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> list = new ArrayList<GrantedAuthority>();
        if(!userrole.isEmpty()){
            list.add(new SimpleGrantedAuthority(ROLE_PREFIX + userrole));
        }        
        return list;
    }

    @Override
    public String getPassword() {
        return password;
    }
    @Override
    public String getUsername() {
        return username;
    }
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    @Override
    public boolean isEnabled() {
        return true;    
    }   
    
}
