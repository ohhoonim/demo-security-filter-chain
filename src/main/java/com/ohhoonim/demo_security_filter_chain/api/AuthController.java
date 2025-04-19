package com.ohhoonim.demo_security_filter_chain.api;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @GetMapping("/main")
    public String main(SecurityContext securityContext) {
        return response(securityContext, "main");
    }

    @PostMapping("/login")
    public String login(SecurityContext securityContext) {
        return response(securityContext, "login");
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
    public String somePage(SecurityContext securityContext) {
        return response(securityContext, "somePage");
    }

    private String response(SecurityContext securityContext, String pageName) {
        var username = securityContext.getAuthentication()
            .getPrincipal();
        return """
                    <h1>%s</h1>
                    <p>username : %s</p>
                """.formatted(pageName, username);

    }

}
