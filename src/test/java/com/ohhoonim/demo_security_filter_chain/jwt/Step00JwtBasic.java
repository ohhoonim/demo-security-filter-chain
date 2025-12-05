package com.ohhoonim.demo_security_filter_chain.jwt;

import static org.assertj.core.api.Assertions.assertThat;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import javax.crypto.SecretKey;
import org.junit.jupiter.api.Test;
import io.jsonwebtoken.security.Keys;

class Step00JwtBasic {

  /*
   * jwt는 header.payload.signature 로 구성된다. 
   * 예) eyAiYWxnIjogIkgyNTYiIH0K.eyAic3ViIjogIkpvZSIgfQo=.ZXlBaVlXeG5Jam9nSW
   */

  @Test
  void base64PlainEncodedJwt() {
    // header + payload 만 우선 보자 
    String header = """
        {
          "alg": "none"
        }
                    """;
    String payload = "this is message in payload";

    String encodedHeader = base64UrlEncoder(header);
    String encodedPayload = base64UrlEncoder(payload);

    String compact = String.format("%s.%s", encodedHeader, encodedPayload);

    assertThat(compact)
        .isEqualTo("ewogICJhbGciOiAibm9uZSIKfQo=.dGhpcyBpcyBtZXNzYWdlIGluIHBheWxvYWQ=");

  }

  private String base64UrlEncoder(String chars) {
    return Base64.getUrlEncoder().encodeToString(chars.getBytes(StandardCharsets.UTF_8));
  }

  @Test
  void jwsClaimExample() {
    // signature는 어떻게 만들어질까
    String header = """
        { "alg": "H256" }
        """;

    String claims = """
        { "sub": "Joe" }
        """;

    String encodedHeader = base64UrlEncoder(header);
    String encodedClaims = base64UrlEncoder(claims);
    String concatenated = String.format("%s.%s", encodedHeader, encodedClaims);

    String keyChars = "vjnmoawioepajsbkbjaoldsjalevmbvmlkasjesklasbv";
    SecretKey key = Keys.hmacShaKeyFor(keyChars.getBytes(StandardCharsets.UTF_8));

    String signature = hmaSha256(concatenated, key);

    String compact = concatenated + "." + base64UrlEncoder(signature);

    assertThat(compact).isEqualTo(
        "eyAiYWxnIjogIkgyNTYiIH0K.eyAic3ViIjogIkpvZSIgfQo=.ZXlBaVlXeG5Jam9nSWtneU5UWWlJSDBLLmV5QWljM1ZpSWpvZ0lrcHZaU0lnZlFvPS5qYXZheC5jcnlwdG8uc3BlYy5TZWNyZXRLZXlTcGVjQGZhNzY0NTgy");

  }

  private String hmaSha256(String concatenated, SecretKey key) {
    // 이 로직은 예시용일 뿐임. 실무에서 사용하면 안됨
    String signature = concatenated + "." + key.toString();
    return signature;
  }
}
