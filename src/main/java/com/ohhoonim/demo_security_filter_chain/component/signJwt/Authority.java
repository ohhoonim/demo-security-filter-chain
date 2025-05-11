package com.ohhoonim.demo_security_filter_chain.component.signJwt;

import org.springframework.security.core.GrantedAuthority;

public record Authority(
    Long id,
    String authority) implements GrantedAuthority {

    @Override
    public String getAuthority() {
        return this.authority;
    }
}
