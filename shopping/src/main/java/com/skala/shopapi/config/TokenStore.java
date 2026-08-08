package com.skala.shopapi.config; // 패키지명 확인!

import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.UUID;

@Component
public class TokenStore {
    // 서버 메모리에 토큰 정보를 유지 (서버 재시작 시 초기화됨)
    private final Map<String, String> tokenCache = new ConcurrentHashMap<>();

    public String generateToken(String customerId) {
        String token = UUID.randomUUID().toString();
        tokenCache.put(token, customerId);
        return token;
    }

    public String getCustomerId(String token) {
        return tokenCache.get(token);
    }

    public void removeToken(String token) {
        tokenCache.remove(token);
    }
}