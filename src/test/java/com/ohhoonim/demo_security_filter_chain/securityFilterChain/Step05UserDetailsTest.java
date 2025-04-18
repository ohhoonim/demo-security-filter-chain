package com.ohhoonim.demo_security_filter_chain.securityFilterChain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import com.ohhoonim.demo_security_filter_chain.api.AuthController;

@WebMvcTest(AuthController.class)
public class Step05UserDetailsTest {
    

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
        // RequestCacheAwareFilter
        // SecurityContextHolderAwareRequestFilter
        // AnonymousAuthenticationFilter
        // ExceptionTranslationFilter
        // AuthorizationFilter
    }


    @Autowired
    MockMvcTester mockMvcTester;

    @Test
    @WithMockUser("ohhoonim")
    public void entpointTest() {
        mockMvcTester.get().uri("/main")
                .assertThat()
                .hasStatus(200)
                .bodyText().isEqualTo("main");
    }

    @Autowired
    PasswordEncoder passwordEncoder;
    
    @Test
    public void passwordEncoderTest() {
        var encodedPassword = passwordEncoder.encode("password");
        var matched = passwordEncoder.matches("password", encodedPassword);
        assertThat(matched).isTrue();

    }

    @Autowired
    UserDetailsService userDetailsService;

    @Test
    public void userDetailsServiceTest() {
        var userDetails = userDetailsService.loadUserByUsername("ohhoonim");
        assertThat(userDetails.getAuthorities()).hasSize(2);
        assertThat(userDetails.getAuthorities().stream().findFirst().get().getAuthority())
                .isNotEqualTo("ROLE_USERB");
                // .isEqualTo("ROLE_USERB");
        
        userDetails.getAuthorities().stream().forEach(auth -> System.out.println(auth));
    }

    @TestConfiguration
    @EnableWebSecurity(debug = true)
    public static class SecurityConfig {
        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http.authorizeHttpRequests(authz -> authz
                    .anyRequest().authenticated());
            return http.build();
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
            return PasswordEncoderFactories.createDelegatingPasswordEncoder(); 
        }

        @Bean UserDetailsService userDetailsService() {
            return new InMemoryUserDetailsManager(
                    User.withUsername("ohhoonim")
                            .password(passwordEncoder().encode("password"))
                            .roles("USERB") // roles 또는 authorities 중 하나만 써야함. 나중에 적용한것이 덮어씀
                            .authorities("TEAM_A", "ROLE_USER")
                            .build());
        }
    }
}
