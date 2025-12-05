package com.ohhoonim.demo_security_filter_chain.authority;

import java.util.function.Supplier;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.authorization.AuthorizationResult;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import com.ohhoonim.demo_security_filter_chain.api.AuthorityController;

@Import({OpenPolicyAuthorizationManager.class, SecurityConfig03.class})
@WebMvcTest(AuthorityController.class)
class Step03AuthorizationManager {

    @Autowired
    MockMvcTester mockMvcTester;

    @Test
    void authorizationManagerTest() {
        mockMvcTester.get().uri("/main").assertThat().hasStatus(HttpStatus.OK);
    }

}


@TestConfiguration
class SecurityConfig03 {

    @Autowired
    OpenPolicyAuthorizationManager authzManager;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) {
        http.authorizeHttpRequests(authz -> authz.anyRequest().access(authzManager)

        );
        return http.build();
    }
}


@Component
class OpenPolicyAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {


    @Override
    public @Nullable AuthorizationResult authorize(
            Supplier<? extends @Nullable Authentication> authentication,
            RequestAuthorizationContext object) {
        // HttpServletRequest request = context.getRequest();
        // Map<String, String> variables = context.getVariables();

        return new AuthorizationDecision(true);
    }
}
