package com.ohhoonim.demo_security_filter_chain.api;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @GetMapping("/main")
    public String main(Authentication authentication) {
        return response(authentication, "main");
    }

    public String login(Authentication authentication) {
        return response(authentication, "login");
    }
    

    @PostMapping("/logout")
    public String logout() {
        return "logout";
    }

    @GetMapping("/refresh")
    public String refresh() {
        return "refresh";
    }

    @GetMapping("/somePage")
    public String somePage(Authentication authentication) {
        return response(authentication, "somePage");
    }

    private String response(Authentication authentication, String pageName) {
        var username = authentication.getPrincipal();
        return """
                    <h1>%s</h1>
                    <p>username : %s</p>
                """.formatted(pageName, username);

    }

}
