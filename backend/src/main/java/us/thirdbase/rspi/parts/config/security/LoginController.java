package us.thirdbase.rspi.parts.config.security;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class LoginController {

    @GetMapping("/login")
    public Principal user(Principal principal) {
        return principal;
    }
}
