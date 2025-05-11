package com.ohhoonim.demo_security_filter_chain.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthorityController {

    @GetMapping("/main/main")
    public String mainPermitAll() {
        return "main permitAll";
    }

    @GetMapping("/main/subMain")
    public String mainSub() {
        return "main sub";
    }
    
    @GetMapping("/role/admin")
    public String adminRole() {
        return "admin only";
    }

    @GetMapping("/privillege/write")
    public String writeOnly() {
        return "write only";
    }
}
