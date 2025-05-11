package com.ohhoonim.demo_security_filter_chain.component.signJwt;

import java.util.Optional;

public interface UserPort {
    void addUser(User newUser);

    Optional<User> findByUsernamePassword(String name, String password);
}