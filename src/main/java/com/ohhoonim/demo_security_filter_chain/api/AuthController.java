package com.ohhoonim.demo_security_filter_chain.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ohhoonim.demo_security_filter_chain.jwt.JwtUtil;

@RestController
public class AuthController {
    
    @GetMapping("/main")
    public String auth() {
        return "main";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username) {
        return JwtUtil.generateToken(username);
        // return "login";
    }
    @PostMapping("/logout")
    public String logout() {
        return "logout";
    }
}
