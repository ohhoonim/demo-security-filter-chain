package com.ohhoonim.demo_security_filter_chain.component.signJwt;

import java.util.List;

public interface AuthorityPort {
    List<Authority> authoritiesByUsername(String name);
}