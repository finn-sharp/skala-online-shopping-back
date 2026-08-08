package com.skala.shopapi.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class TokenStore {

    @Value("${jwt.secret}")
    private String secretKeyString; // 설정 파일(yml)에서 주입받는 시크릿 키 문자열

    @Value("${jwt.expiration}")
    private long tokenValidTime; // 설정 파일(yml)에서 주입받는 토큰 유효 시간

    private Key key;

    // 빈이 생성된 후, 주입받은 문자열 시크릿 키를 HMAC-SHA 알고리즘 Key 객체로 변환
    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secretKeyString.getBytes());
    }

    // 1. JWT 토큰 생성 (role 정보 포함)
    public String generateToken(String customerId, String role) {
        Claims claims = Jwts.claims().setSubject(customerId);
        claims.put("role", role); // <-- role을 페이로드(클레임)에 추가
        
        Date now = new Date();
        Date validity = new Date(now.getTime() + tokenValidTime); // 선언된 변수명(tokenValidTime) 사용

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(key) // init()에서 생성해둔 Key 객체 사용
                .compact();
    }

    // 2. JWT 토큰에서 고객 ID 추출 (검증 포함)
    public String getCustomerId(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    // 3. 토큰 유효성 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            // 위조되었거나, 만료되었거나, 형식이 틀린 경우
            return false;
        }
    }
    
    // 4. JWT 토큰에서 권한(role) 추출
    public String getRole(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.get("role", String.class);
    }
}