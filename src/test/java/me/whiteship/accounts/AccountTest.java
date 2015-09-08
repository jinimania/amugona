package me.whiteship.accounts;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author LeeSooHoon
 */
public class AccountTest {

    @Test
    public void getterSetter() {
        Account account = new Account();
        account.setUserName("whiteship");
        account.setPassword("password");
        assertThat(account.getUserName(), is("whiteship"));
    }
}
