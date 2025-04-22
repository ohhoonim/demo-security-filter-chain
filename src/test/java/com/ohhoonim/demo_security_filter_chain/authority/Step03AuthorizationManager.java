package com.ohhoonim.demo_security_filter_chain.authority;

import java.util.function.Supplier;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import com.ohhoonim.demo_security_filter_chain.api.AuthorityController;

@Import(OpenPolicyAuthorizationManager.class)
@WebMvcTest(AuthorityController.class)
public class Step03AuthorizationManager {

    @Autowired
    MockMvcTester mockMvcTester;

    @Test
    public void authorizationManagerTest() {
        mockMvcTester.get().uri("/main")
                .assertThat()
                .hasStatus(HttpStatus.OK);
    }

    @TestConfiguration
    public static class SecurityConfig {

        @Autowired
        OpenPolicyAuthorizationManager authzManager;

        @Bean
        SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http.authorizeHttpRequests(authz -> authz
                    .anyRequest().access(authzManager)

            );
            return http.build();
        }
    }
}

@Component
class OpenPolicyAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

    @Override
    @Nullable
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext context) {

        // HttpServletRequest request = context.getRequest();
        // Map<String, String> variables = context.getVariables();

        return new AuthorizationDecision(true);
    }
}