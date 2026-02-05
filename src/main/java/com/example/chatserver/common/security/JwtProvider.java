package com.example.chatserver.common.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;

@Component
public class JwtProvider {

    private final Key key;

    /**
     * 서명 검증용 Key 생성
     */
    public JwtProvider(@Value("${jwt.secret}") String secret) {

        byte[] bytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(bytes);
    }

    /**
     * Authorization 헤더에서 userId 추출
     */
    public Long extractUserId(String authorization) {

        if (authorization == null || !authorization.startsWith("Bearer ")) {
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
        }

        String token = authorization.substring(7);

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return Long.valueOf(claims.getSubject());
    }
}
