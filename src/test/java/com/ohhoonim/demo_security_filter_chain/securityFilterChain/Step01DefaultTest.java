package com.ohhoonim.demo_security_filter_chain.securityFilterChain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import com.ohhoonim.demo_security_filter_chain.api.AuthController;

@WebMvcTest(AuthController.class)
public class Step01DefaultTest {

    // step 01 : spring security dependency 만 추가한 경우

    @Test
    public void loadContext() {
        // SecurityFilterChain 
        // /////////////// 
        //   DisableEncodeUrlFilter
        //   WebAsyncManagerIntegrationFilter
        //   SecurityContextHolderFilter
        //   HeaderWriterFilter
        //   CsrfFilter
        //   LogoutFilter
        //   UsernamePasswordAuthenticationFilter
        //   DefaultResourcesFilter
        //   DefaultLoginPageGeneratingFilter
        //   DefaultLogoutPageGeneratingFilter
        //   BasicAuthenticationFilter
        //   RequestCacheAwareFilter
        //   SecurityContextHolderAwareRequestFilter
        //   AnonymousAuthenticationFilter
        //   ExceptionTranslationFilter
        //   AuthorizationFilter
    }

    @Autowired
    MockMvcTester mockMvcTester;

    @Test
    public void endpointTest() {
        mockMvcTester.get().uri("/main")
                .assertThat()
                .hasStatus(HttpStatus.UNAUTHORIZED);
    }

    @TestConfiguration
    @EnableWebSecurity(debug = true)
    public static class SecurityConfig {
        
    }
}
