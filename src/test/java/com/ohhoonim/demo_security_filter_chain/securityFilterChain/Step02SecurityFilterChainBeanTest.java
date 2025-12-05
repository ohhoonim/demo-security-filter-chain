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
@Import(SecurityConfig02.class)
class Step02SecurityFilterChainBeanTest {

    // step 02 : SecurityFilterChain bean을 default로 등록한 경우

    @Test
    public void loadContext() {
        // SecurityFilterChain 
        // /////////////// 
        // DisableEncodeUrlFilter
        // WebAsyncManagerIntegrationFilter
        // SecurityContextHolderFilter
        // HeaderWriterFilter
        // CsrfFilter
        // LogoutFilter
        //// 사라짐//////// UsernamePasswordAuthenticationFilter 사라짐//////// DefaultResourcesFilter
        //// 사라짐//////// DefaultLoginPageGeneratingFilter 사라짐////////
        /// DefaultLogoutPageGeneratingFilter 사라짐//////// BasicAuthenticationFilter
        // RequestCacheAwareFilter
        // SecurityContextHolderAwareRequestFilter
        // AnonymousAuthenticationFilter
        // ExceptionTranslationFilter
        //// 사라짐//////// AuthorizationFilter
    }

    @Autowired
    MockMvcTester mockMvcTester;

    @Test
    void endpointTest() {
        mockMvcTester.get().uri("/main").assertThat().hasStatus(HttpStatus.OK);
        // .bodyText().isEqualTo("main");
    }

}


@TestConfiguration
@EnableWebSecurity(debug = true)
class SecurityConfig02 {
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http.build();
    }
}
