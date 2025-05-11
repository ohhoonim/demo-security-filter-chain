package com.ohhoonim.demo_security_filter_chain.component.signJwt;

import java.util.List;
import java.util.UUID;

public record User(
        UUID id,
        String name,
        String password,
        List<Authority> authorities) {
    public User(String username, String password) {
        this(null, username, password, null);
    }
}
