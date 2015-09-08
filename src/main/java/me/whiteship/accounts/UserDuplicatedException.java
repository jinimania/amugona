package me.whiteship.accounts;

/**
 * @author LeeSooHoon
 */
public class UserDuplicatedException extends RuntimeException {

    String userName;

    public UserDuplicatedException(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }
}
