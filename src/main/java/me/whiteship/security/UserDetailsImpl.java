package me.whiteship.security;

import me.whiteship.accounts.Account;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author by LeeSoohoon
 */
public class UserDetailsImpl extends User {
    public UserDetailsImpl(Account account) {
        super(account.getUserName(), account.getPassword(), authorities(account));
    }

    private static Collection<? extends GrantedAuthority> authorities(Account account) {
        final List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        if (account.isAdmin()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }
        return authorities;
    }
}
