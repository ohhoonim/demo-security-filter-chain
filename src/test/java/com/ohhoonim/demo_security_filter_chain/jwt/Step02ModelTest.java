package com.ohhoonim.demo_security_filter_chain.jwt;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@ExtendWith(MockitoExtension.class)
public class Step02ModelTest {

    Logger log = LoggerFactory.getLogger(this.getClass());

    SignUsecase signService;

    @Mock
    UserPort userPort;

    @Mock
    AuthorityPort authorityPort;

    @Mock
    BearerTokenUsecase tokenService;

    @BeforeEach
    public void setUp() {
        signService = new SignService(userPort,
                authorityPort,
                tokenService);
    }

    @Test
    @DisplayName("회원가입")
    public void signUpTest() {
        var newUser = new User("matthew", "secret");
        signService.signUp(newUser);

        verify(userPort, times(1)).addUser(any());
    }

    @Test
    @DisplayName("회원 로그인")
    public void signInTest() {
        var loginTryUser = new User("matthew", "secret");

        when(userPort.findByUsernamePassword(any(), any())).thenReturn(Optional.of(new User("matthew", null)));
        when(tokenService.generateAccessToken(any(), any())).thenReturn("jsdkalfsdajkflsdajfk");
        when(tokenService.generateRefreshToken(any(), any())).thenReturn("svkalsajekf");

        SignVo signInVo = signService.signIn(loginTryUser);

        assertThat(signInVo.access()).isEqualTo("jsdkalfsdajkflsdajfk");
        assertThat(signInVo.refresh()).isEqualTo("svkalsajekf");
    }

    @Test
    public void signFailTest() {
        var loginTryUser = new User("matthew", "secret");
        when(userPort.findByUsernamePassword(any(), any())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> signService.signIn(loginTryUser))
                .hasMessage("사용자를 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("회원 로그아웃")
    public void signOutTest() {
        // todo 로그아웃을 도메인 모델에서 뭘 해야할까?
    }

    @Test
    @DisplayName("refresh token 요청")
    public void refreshTokenTest() {
        String refreshToken = "jsdkalfjsdaklv";

        when(tokenService.getUsername(any())).thenReturn("matthew");

        when(tokenService.generateAccessToken(any(), any())).thenReturn("newABARSDFSDf");
        when(tokenService.generateRefreshToken(any(), any())).thenReturn("svkalsajekf");

        SignVo sign = signService.refresh(refreshToken);
        assertThat(sign.access()).isEqualTo("newABARSDFSDf");
        assertThat(sign.refresh()).isEqualTo("svkalsajekf");
    }

    @Test
    public void generateTokenTest() {
        BearerTokenService tokenService = new BearerTokenService();
        String access = tokenService.generateAccessToken("matthew", List.of());
        log.info("access : {}", access);

        String username = tokenService.getUsername(access);
        assertThat(username).isEqualTo("matthew");

    }

    @Test
    public void expiredTimeTest() {
        BearerTokenService tokenService = new BearerTokenService();
        String access = tokenService.generateDenyToken("matthew", List.of());
        log.info("access : {}", access);

        assertThatThrownBy(() -> tokenService.getExpired(access))
                .isInstanceOf(ExpiredJwtException.class)
                .hasMessageContaining("expired");

    }

}

record SignVo(
        String access,
        String refresh) {
}

/////////////////////////////////record
/// 
record User(
        UUID id,
        String name,
        String password,
        List<Authority> authorities) {
    public User(String name, String password) {
        this(null, name, password, null);
    }
}

record Authority(
        Long id,
        String authority) {
}

///////////////////////////////usecase sign
interface SignUsecase {

    void signUp(User newUser);

    SignVo refresh(String refreshToken);

    SignVo signIn(User loginTryUser);

}

class SignService implements SignUsecase {

    private final UserPort userPort;
    private final BearerTokenUsecase bearerTokenService;
    private final AuthorityPort authorityPort;

    public SignService(UserPort userPort,
            AuthorityPort authorityPort,
            BearerTokenUsecase bearerTokenService) {
        this.userPort = userPort;
        this.authorityPort = authorityPort;
        this.bearerTokenService = bearerTokenService;
    }

    @Override
    public void signUp(User newUser) {
        userPort.addUser(newUser);
    }

    @Override
    public SignVo signIn(User loginTryUser) {
        var user = userPort.findByUsernamePassword(loginTryUser.name(), loginTryUser.password());
        if (user.isPresent()) {
            return generateTokenVo(user.get().name());
        } else {
            throw new RuntimeException("사용자를 찾을 수 없습니다.");
        }
    }

    @Override
    public SignVo refresh(String refreshToken) {
        try {
            String userName = bearerTokenService.getUsername(refreshToken);
            return generateTokenVo(userName);
        } catch (Exception e) {
            throw new RuntimeException("토큰이 유효하지 않습니다.");
        }
    }

    private SignVo generateTokenVo(String userName) {
        List<Authority> authorities = authorityPort.authoritiesByUsername(userName);
        String access = bearerTokenService.generateAccessToken(userName, authorities);
        String refresh = bearerTokenService.generateRefreshToken(userName, authorities);
        return new SignVo(access, refresh);
    }

}

interface UserPort {
    void addUser(User newUser);

    Optional<User> findByUsernamePassword(String name, String password);
}

interface AuthorityPort {
    List<Authority> authoritiesByUsername(String name);
}

////////////////////////////////////usecase jwt
interface BearerTokenUsecase {
    String generateAccessToken(String userName, List<Authority> authorities);

    String getUsername(String refreshToken);

    Date getExpired(String refreshToken);

    String generateRefreshToken(String userName, List<Authority> authorities);

    // for test
    String generateDenyToken(String userName, List<Authority> authorities);
}

class BearerTokenService implements BearerTokenUsecase {
    Logger log = LoggerFactory.getLogger(BearerTokenService.class);
    private String keyChar = "e3f1a5c79b2d4f8e6a1c3d7f9b0e2a4c5d6f8a9b3c1d7e4f2a6b8c0d9e7f3a1";
    private SecretKey key;

    public BearerTokenService() {
        key = Keys.hmacShaKeyFor(keyChar.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public String generateAccessToken(String userName, List<Authority> authorities) {
        return generateToken(userName, authorities, TokenType.ACCESS);
    }

    @Override
    public String getUsername(String refreshToken) {
        return Jwts.parser().verifyWith(key).build()
                .parseSignedClaims(refreshToken)
                .getPayload().getSubject();
    }

    @Override
    public Date getExpired(String refreshToken) {
        return Jwts.parser().verifyWith(key).build()
                .parseSignedClaims(refreshToken)
                .getPayload().getExpiration();
    }

    @Override
    public String generateRefreshToken(String userName, List<Authority> authorities) {
        return generateToken(userName, authorities, TokenType.REFRESH);
    }

    @Override
    public String generateDenyToken(String userName, List<Authority> authorities) {
        return generateToken(userName, authorities, TokenType.DENY);
    }

    private String generateToken(String username, List<Authority> authorities, TokenType tokenType) {
        return Jwts.builder()
                .subject(username)
                .claim("roles",
                        authorities.stream()
                                .map(Authority::authority)
                                .collect(Collectors.joining(",")))
                .expiration(tokenType.ext())
                .issuedAt(Date.from(LocalDateTime.now().toInstant(ZoneOffset.of("+09:00"))))
                .signWith(key)
                .compact();
    }

    public enum TokenType {
        ACCESS(Date.from(LocalDateTime.now().plusHours(1).toInstant(ZoneOffset.of("+09:00")))),
        REFRESH(Date.from(LocalDateTime.now().plusDays(7).toInstant(ZoneOffset.of("+09:00")))),
        DENY(Date.from(LocalDateTime.now().plusDays(-7).toInstant(ZoneOffset.of("+09:00"))));

        private final Date exp;

        TokenType(Date exp) {
            this.exp = exp;
        }

        public Date ext() {
            return this.exp;
        }
    }

}
/*
 */
/*
# Product Requirements Document

## 회원가입
생략

## 가입승인
생략

## 로그인
- 아이디/패스워드 기반 로그인
- 로그인 시도시 아이디/패스워드가 일치하면 json web token을 리턴한다

```plantuml
@startuml 
left to right direction

title Sign-Jwt Model : Usecase

actor 비회원
actor 회원
actor 관리자

usecase 회원가입
usecase 로그인
usecase 회원탈퇴
usecase 가입승인

비회원 --> 회원가입
회원 --> 로그인
회원 --> 회원탈퇴
관리자 --> 가입승인

@enduml
```

```
@startuml
left to right direction

title Sign-Jwt Model : logical > class

class User {
    id: UUID
    name: string
    password: string
    getName(): string
    findByUsernamePassword(): User [0..1]
    getAuthorities(): Authority [0..*]
    addAuthorites(): void
    removeAuthrity(authority: Authority): void
}

class Authority {
    id: UUID
    authrity: string
    searchByAuthority(): Authority [0..*]
}

User "1" ---- "authorities 0..*" Authority

class SignVo {
    access: String
    refresh: String
}

@enduml
```

## Sign-Jwt Model : Logical > sequence 

```plantuml
@startuml
skinparam monochrome true 
autonumber

title Sign-Jwt Model : Logical > sequence 

actor       회원 as member
boundary    로그인페이지  as login_form
control     로그인서비스   as login_service
control     JWT서비스   as jwt_service
database    회원DB      as db
database    토큰캐쉬      as redis 
boundary    Security필터      as jwt_filter
boundary    서비스페이지   as service_page

member -> login_form: 로그인폼 접속
member -> login_form: 아이디/패스워드 입력
login_form -> login_service : 로그인
login_service -> db : 아이디 패스워드 일치 확인
alt 일치하면
    login_service -> db : grantedAuthority 조회
    db --> login_service : grantedAuthority 
else 불일치
    login_service --> login_form : 아이디/패스워드 재입력 요청
end
login_service -> jwt_service : 토큰 생성 요청
jwt_service --> login_service : 토큰 발행
jwt_service --> redis : 토큰 저장
login_service --> login_form : 토큰 전송 (access, refresh)
== 토큰 만료시 refresh ==
member -> service_page : 페이지 접근
service_page --> jwt_filter : 토큰 검증
alt 검증 실패
    jwt_filter --> service_page : token 만료  
    service_page -> login_service : refresh 요청
    login_service -> jwt_service : refresh 토큰 검증
    jwt_service --> login_service : (정상시) 신규 access/refresh 토큰 발급
    jwt_service --> redis : 신규 access/refresh 저장, 기존 제거
    login_service -> service_page : 신규 access/refresh 토큰 전송 
    service_page -> jwt_filter : 신규 access 토큰 검증
    jwt_filter -> service_page : (검증성공)서비스페이지로 이동 
else 통과 
    jwt_filter -> service_page : 서비스페이지 이동  
end

jwt_filter -> service_page : 서비스 페이지 이동

@enduml
```

## 회원탈퇴

 */