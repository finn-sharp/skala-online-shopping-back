@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(auth -> auth
            // 1. Swagger 관련 페이지는 누구나 접근 가능
            .requestMatchers(
                "/swagger-ui/**",
                "/v3/api-docs/**",
                "/swagger-resources/**",
                "/webjars/**"
            ).permitAll()

            // 2. [기능별 설정 예시] 회원가입, 로그인 API는 인증 없이 누구나 접근 가능
            .requestMatchers("/api/customers/signup", "/api/customers/login").permitAll()

            // 3. [기능별 설정 예시] 상품 조회 같은 누구나 볼 수 있는 공용 API는 허용
            .requestMatchers("/api/products/**").permitAll()

            // 4. 그 외의 모든 요청(마이페이지, 주문, 장바구니 등)은 반드시 인증(토큰) 필요
            .anyRequest().authenticated()
        )
        .formLogin(form -> form.permitAll());

    return http.build();
}