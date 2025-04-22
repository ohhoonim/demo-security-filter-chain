package com.ohhoonim.demo_security_filter_chain.authority;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreFilter;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Service;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Import(BlogService.class)
@WebMvcTest(BlogController.class)
// @ContextConfiguration
public class Step04AuthorityByMethodTest {

    @Autowired
    MockMvcTester mockMvcTester;

    @Test
    @WithMockUser(authorities = { "ROLE_ADMIN" , "write"})
    public void authorityControllerTest() throws Exception {
        mockMvcTester.get().uri("/blog")
                .assertThat()
                .hasStatus(HttpStatus.OK);
    }

    @Autowired
    BlogService blogService;

    @Test
    @WithMockUser(authorities = { "ROLE_ADMIN", "read" })
    public void serviceMethodTest() throws Exception {
        assertThatThrownBy(() -> blogService.blogWrite())
                .isInstanceOf(AuthorizationDeniedException.class);
    }

    @TestConfiguration
    @EnableMethodSecurity
    public static class SecurityConfig {
        @Bean
        SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http.authorizeHttpRequests(authz -> authz
                    .requestMatchers("/blog/**").hasAuthority("ROLE_ADMIN")
                    .anyRequest().authenticated());
            return http.build();
        }
    }
}

@Service
class BlogService {

    @PreAuthorize("hasAuthority('write')")
    String blogWrite() {
        return "write blog success";
    }
}

@RestController
class BlogController {

    private final BlogService blogService;

    BlogController(BlogService blogService) {
        this.blogService = blogService;
    }

    @GetMapping("/blog")
    public String blog() {
        return "blog";
    }

    @GetMapping("/blog/details")
    public String blogDetails() {
        return blogService.blogWrite();
    }
}
