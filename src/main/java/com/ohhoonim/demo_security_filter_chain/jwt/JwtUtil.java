package com.ohhoonim.demo_security_filter_chain.jwt;

import java.security.Key;
import java.util.Date;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureAlgorithm;

public class JwtUtil {
    private static final String SECRET_KEY = "your-256-bit-secret-your-256-bit-secret"; // 256비트 키
    private static final long EXPIRATION_TIME = 1000 * 60 * 60; // 1시간

    private static final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    // JWT 생성
    public static String generateToken(String username) {
        // return Jwts.builder()
        //         .setSubject(username)
        //         .setIssuedAt(new Date())
        //         .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
        //         .signWith(key, SignatureAlgorithm)
        //         .compact();
        return null;
    }

    // JWT 검증 및 파싱
    public static Claims validateToken(String token) {
        // return Jwts.parserBuilder()
        //         .setSigningKey(key)
        //         .build()
        //         .parseClaimsJws(token)
        //         .getBody();
        return null;
    }
}
