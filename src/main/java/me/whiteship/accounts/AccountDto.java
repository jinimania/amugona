package me.whiteship.accounts;

import org.hibernate.validator.constraints.NotBlank;

import java.util.Date;

import javax.validation.constraints.Size;

import lombok.Data;

/**
 * @author LeeSooHoon
 */
public class AccountDto {

    @Data
    public static class Create {
        @NotBlank
        @Size(min = 5)
        private String userName;

        @NotBlank
        @Size(min = 5)
        private String password;
    }

    @Data
    public static class Response {
        private Long id;
        private String userName;
        private String fullName;
        private Date joined;
        private Date updated;
    }
}
