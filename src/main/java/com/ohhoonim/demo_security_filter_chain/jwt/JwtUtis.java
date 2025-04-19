package com.ohhoonim.demo_security_filter_chain.jwt;

import java.security.Key;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtis {

    private static final String SECRET_KEY = "your-256-bit-secret-your-256-bit-secret"; // 256비트 키
    private static final long EXPIRATION_TIME = 1000 * 60 * 60; // 1시간
    private static final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    public String generateToken(String username) {
        // TODO 토큰 생성
        return null;
    }

    public Claims validateToken(String token) {
        // TODO 토큰 검증
        return null;
    }
}
