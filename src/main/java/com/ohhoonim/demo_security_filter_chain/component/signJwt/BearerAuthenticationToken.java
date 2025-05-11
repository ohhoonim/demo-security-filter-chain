package com.ohhoonim.demo_security_filter_chain.component.signJwt;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class BearerAuthenticationToken extends AbstractAuthenticationToken{

    public BearerAuthenticationToken(Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
    }


    @Override
    public Object getPrincipal() {
        return "matthew";
    }


    @Override
    public Object getCredentials() {
        return null;
    }
    
}
