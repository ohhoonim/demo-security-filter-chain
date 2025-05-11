package com.ohhoonim.demo_security_filter_chain.component.signJwt;

public interface SignUsecase {
    void signUp(User newUser);

    SignVo refresh(String refreshToken);

    SignVo signIn(User loginTryUser);
}
