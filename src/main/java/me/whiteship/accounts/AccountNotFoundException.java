package me.whiteship.accounts;

/**
 * @author LeeSooHoon
 */
public class AccountNotFoundException extends RuntimeException {

    Long id;

    public AccountNotFoundException(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
