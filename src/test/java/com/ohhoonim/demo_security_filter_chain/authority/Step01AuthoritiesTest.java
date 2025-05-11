package com.ohhoonim.demo_security_filter_chain.authority;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import com.ohhoonim.demo_security_filter_chain.api.AuthorityController;

@WebMvcTest(AuthorityController.class)
public class Step01AuthoritiesTest {

    @Autowired
    MockMvcTester mockMvcTester;

    @Test
    public void permitAllTest() {
        mockMvcTester.get().uri("/main/main")
            .assertThat()
            .hasStatus(HttpStatus.OK);
    }

    @Test
    public void mainSubPermitAllTest() {
        mockMvcTester.get().uri("/main/subMain")
            .assertThat()
            .hasStatus(HttpStatus.OK);
    }

    @Test
    public void authenticatedTest() {
        mockMvcTester.get().uri("/role/admin")
            .assertThat()
            .hasStatus(HttpStatus.FORBIDDEN); // 403
    }

    @TestConfiguration
    @EnableWebSecurity(debug = true)
    static class SecurityConfig {
        @Bean
        SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http.authorizeHttpRequests(authz -> authz
                    .requestMatchers("/main/**").permitAll()
                    .anyRequest().authenticated());

            return http.build();
        }
    }
}
