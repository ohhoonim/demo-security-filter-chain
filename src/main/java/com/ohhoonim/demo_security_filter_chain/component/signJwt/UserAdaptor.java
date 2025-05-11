package com.ohhoonim.demo_security_filter_chain.component.signJwt;

import java.util.Optional;

import org.springframework.stereotype.Component;

@Component
public class UserAdaptor implements UserPort{

    @Override
    public void addUser(User newUser) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addUser'");
    }

    @Override
    public Optional<User> findByUsernamePassword(String name, String password) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByUsernamePassword'");
    }

    
}
