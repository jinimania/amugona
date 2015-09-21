package me.whiteship.security;

import me.whiteship.accounts.Account;
import me.whiteship.accounts.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author by LeeSoohoon
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final Account account = accountRepository.findByUserName(username);
        if (account == null) {
            throw new UsernameNotFoundException(username);
        }
        return new UserDetailsImpl(account);
    }
}
