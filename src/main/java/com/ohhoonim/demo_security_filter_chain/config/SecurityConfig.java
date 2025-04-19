package com.ohhoonim.demo_security_filter_chain.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;

import com.ohhoonim.demo_security_filter_chain.jwt.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity(debug = true)
public class SecurityConfig {

    
    
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http,
            JwtAuthenticationFilter jwtAuthenticationFilter 
    ) throws Exception {
        http.csrf(c -> c.disable());

        http.authorizeHttpRequests(authz -> authz
            .requestMatchers("/main",
                    "/signup", 
                    "/login", 
                    "/refresh").permitAll()
            .anyRequest().authenticated()
        );

        // http.formLogin(Customizer.withDefaults());

        http.logout(logout -> logout
            .logoutUrl("/logout")
            .logoutSuccessUrl("/main")     
        );

        http.sessionManagement(s -> s
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        http.addFilterAfter(jwtAuthenticationFilter, LogoutFilter.class);
        // http.addFilterBefore(jwtAuthenticationFilter,
                // UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}

// DisableEncodeUrlFilter
// WebAsyncManagerIntegrationFilter
// SecurityContextHolderFilter
// HeaderWriterFilter
// LogoutFilter

// JwtAuthenticationFilter

// RequestCacheAwareFilter
// SecurityContextHolderAwareRequestFilter
// AnonymousAuthenticationFilter
// SessionManagementFilter
// ExceptionTranslationFilter

// AuthorizationFilter
/////////////////////////////////////////////
// UsernamePassword관련은 formLogin()과 관련된 것들이며
// AuthenticationManager, AuthenticationProvider
// UserDetailsService, UserDetails 
// 또한 그렇다.
// Authentication은 SecurityContext에 보관되는 토큰이다.
// 
