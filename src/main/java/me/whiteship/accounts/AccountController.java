package me.whiteship.accounts;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author LeeSooHoon
 */
@RestController
public class AccountController {

    @RequestMapping("/hello")
    public String hello() {
        return "Hello Spring Boot";
    }
}
