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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import com.ohhoonim.demo_security_filter_chain.api.AuthorityController;
import com.ohhoonim.demo_security_filter_chain.component.signJwt.AuthorityAdaptor;
import com.ohhoonim.demo_security_filter_chain.component.signJwt.BearerTokenService;

@WebMvcTest(AuthorityController.class)
@Import({BearerTokenService.class, AuthorityAdaptor.class, SecurityConfig.class})
class Step02RoleTest {

    @Autowired
    MockMvcTester mockMvcTester;

    @Test
    @WithMockUser(roles = {"ADMIN", "USER"})
    void adminRoleTest() {
        mockMvcTester.get().uri("/role/admin").assertThat().hasStatus(HttpStatus.FORBIDDEN);
    }

    @Test
    @WithMockUser(authorities = {"read", "write", "update"})
    void userAuthoritiesTest() {
        mockMvcTester.get().uri("/privillege/write").assertThat().hasStatus(HttpStatus.FORBIDDEN);
    }

    @Test
    @WithMockUser(authorities = "write")
    void userAuthorityTest() {
        mockMvcTester.get().uri("/privillege/write").assertThat().hasStatus(HttpStatus.FORBIDDEN);
    }


}


@TestConfiguration
@EnableWebSecurity(debug = true)
class SecurityConfig {
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) {
        http.authorizeHttpRequests(authz -> authz.requestMatchers("/role/admin").hasRole("ADMIN") // ROLE_
                .requestMatchers("/privillege/write")
                // .hasAuthority("write")
                .access(new WebExpressionAuthorizationManager(
                        "hasAuthority('write') && hasAuthority('read')"))
                .anyRequest().authenticated());
        return http.build();
    }

}
