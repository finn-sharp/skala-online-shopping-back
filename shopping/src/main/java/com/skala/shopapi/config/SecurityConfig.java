package com.skala.shopapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

// import com.skala.shopapi.security.JwtAuthenticationFilter;
// import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final TokenStore tokenStore;

    // TokenStore를 생성자 주입받음
    public SecurityConfig(TokenStore tokenStore) {
        this.tokenStore = tokenStore;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                // 1. Swagger 관련 경로 예외 처리
                .requestMatchers(
                    "/swagger-ui/**",
                    "/v3/api-docs/**",
                    "/swagger-resources/**",
                    "/webjars/**",
                    "/swagger-ui.html"
                ).permitAll()
                
                // 2. 로그인, 회원가입 등 누구나 접근 가능한 API 경로 추가 가능
                // .requestMatchers("/api/customers/login", "/api/customers/signup").permitAll()
                
                // 3. 그 외 모든 요청은 인증 필요
                .anyRequest().authenticated()
            );
            // // 4. JWT 인증 필터 등록
            // .addFilterBefore(new JwtAuthenticationFilter(tokenStore), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}