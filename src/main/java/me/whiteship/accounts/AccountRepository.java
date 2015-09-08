package me.whiteship.accounts;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author LeeSooHoon
 */
public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findByUserName(String userName);
}
