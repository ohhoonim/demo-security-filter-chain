package com.ohhoonim.demo_security_filter_chain.jwt;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

import javax.crypto.SecretKey;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

public class Step01JjwtLibTest {
    Logger log = LoggerFactory.getLogger(Step01JjwtLibTest.class);

    // build.gradle에 아래 dependency 추가
    //    implementation 'io.jsonwebtoken:jjwt-api:0.12.6'
    //    runtimeOnly 'io.jsonwebtoken:jjwt-impl:0.12.6'
    //    runtimeOnly 'io.jsonwebtoken:jjwt-jackson:0.12.6'

    @Test
    public void jjwtLibQuickstart() {
        
        String keyChar = "e3f1a5c79b2d4f8e6a1c3d7f9b0e2a4c5d6f8a9b3c1d7e4f2a6b8c0d9e7f3a1";
        SecretKey key = Keys.hmacShaKeyFor(keyChar.getBytes(StandardCharsets.UTF_8));
        // SecretKey key = Jwts.SIG.HS256.key().build();

        String jws = Jwts.builder()
                .subject("Joe") // 일반적으로 아이디
                .claim("role", "admin") 
                .expiration(Date.from(LocalDateTime.now().plusHours(1).toInstant(ZoneOffset.of("+09:00"))))
                .issuedAt(Date.from(LocalDateTime.now().toInstant(ZoneOffset.of("+09:00"))))
                .signWith(key)
                .compact();

        log.info(jws);

        Jws<Claims> claims = Jwts.parser().verifyWith(key).build()
                .parseSignedClaims(jws);

        assertThat(claims.getPayload().getSubject()).isEqualTo("Joe");
    }

}
