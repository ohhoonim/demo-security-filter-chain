package com.ohhoonim.demo_security_filter_chain.securityFilterChain;

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
import com.ohhoonim.demo_security_filter_chain.api.AuthController;

@WebMvcTest(AuthController.class)
@Import(SecurityConfig04.class)
class Step04AuthorizeHttpRequestsTest {

    // step 04 : authorizeHttpRequests()를 사용한 경우

    @Test
    void loadContext() {
        // SecurityFilterChain
        // ///////////////
        // DisableEncodeUrlFilter
        // WebAsyncManagerIntegrationFilter
        // SecurityContextHolderFilter
        // HeaderWriterFilter
        // CsrfFilter
        // LogoutFilter
        // RequestCacheAwareFilter
        // SecurityContextHolderAwareRequestFilter
        // AnonymousAuthenticationFilter
        // ExceptionTranslationFilter
        // AuthorizationFilter             // <- 이게 생겨남
    }

    // (참고) 
    // AuthorizationFilter는 Spring Security 5.5부터 추가됨
    // 기존의 FilterSecurityInterceptor를 대체한다(Deprecated) 
    // authorizedRequest()를 사용하지 말고 
    // authorizeHttpRequests()를 사용

    @Autowired
    MockMvcTester mockMvcTester;

    @Test
    void endpointTest() {
        mockMvcTester.get().uri("/main").assertThat().hasStatus(HttpStatus.FORBIDDEN);
    }


}


@TestConfiguration
@EnableWebSecurity(debug = true)
class SecurityConfig04 {
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authz -> authz.anyRequest().authenticated());

        return http.build();
    }
}
