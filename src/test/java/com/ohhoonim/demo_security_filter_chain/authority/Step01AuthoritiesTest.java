package com.ohhoonim.demo_security_filter_chain.authority;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import com.ohhoonim.demo_security_filter_chain.api.AuthorityController;
import com.ohhoonim.demo_security_filter_chain.component.signJwt.AuthorityAdaptor;
import com.ohhoonim.demo_security_filter_chain.component.signJwt.BearerTokenService;

@WebMvcTest(AuthorityController.class)
@Import({BearerTokenService.class, AuthorityAdaptor.class})
class Step01AuthoritiesTest {

    @Autowired
    MockMvcTester mockMvcTester;

    @Test
    void permitAllTest() {
        mockMvcTester.get().uri("/main/main").assertThat().hasStatus(HttpStatus.OK);
    }

    @Test
    void mainSubPermitAllTest() {
        mockMvcTester.get().uri("/main/subMain").assertThat().hasStatus(HttpStatus.OK);
    }

    @Test
    void authenticatedTest() {
        mockMvcTester.get().uri("/role/admin").assertThat().hasStatus(HttpStatus.FORBIDDEN); // 403
    }

    @TestConfiguration
    @EnableWebSecurity(debug = true)
    static class SecurityConfig {
        @Bean
        SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http.authorizeHttpRequests(authz -> authz.requestMatchers("/main/**").permitAll()
                    .anyRequest().authenticated());

            return http.build();
        }
    }
}
