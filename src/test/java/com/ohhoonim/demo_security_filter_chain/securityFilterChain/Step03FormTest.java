package com.ohhoonim.demo_security_filter_chain.securityFilterChain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import com.ohhoonim.demo_security_filter_chain.api.AuthController;

@WebMvcTest(AuthController.class)
@Import(SecurityConfig03.class)
class Step03FormTest {

    // step 03 : form login을 추가한 경우

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
        ////////////////////////////////////////////추가됨
        // UsernamePasswordAuthenticationFilter
        // DefaultResourcesFilter // default
        // DefaultLoginPageGeneratingFilter // default
        // DefaultLogoutPageGeneratingFilter // default
        //////////////////////////////////////////////
        // BasicAuthenticationFilter 는 httpBasic을 사용할 때 활성화됨 
        //////////////////////////////////////////////
        // RequestCacheAwareFilter
        // SecurityContextHolderAwareRequestFilter
        // AnonymousAuthenticationFilter
        // ExceptionTranslationFilter
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
class SecurityConfig03 {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // formLogin()의 상세사용법은 해당 api 문서를 참고하거나 아래링크 참조
        // https://docs.spring.io/spring-security/reference/servlet/authentication/passwords/form.html
        http.formLogin(Customizer.withDefaults());

        // http.httpBasic(Customizer.withDefaults());

        return http.build();
    }
}
