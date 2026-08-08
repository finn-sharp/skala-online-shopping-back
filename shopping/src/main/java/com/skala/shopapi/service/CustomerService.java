package com.skala.shopapi.service;

import com.skala.shopapi.data.dto.CustomerLoginRequestDto;
import com.skala.shopapi.data.dto.CustomerLoginResponseDto;
import com.skala.shopapi.data.dto.OrderRequestDto;
import com.skala.shopapi.data.table.Customer;
import com.skala.shopapi.repository.CustomerRepository;
import com.skala.shopapi.config.TokenStore;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenStore tokenStore;

    // 1. 전체 고객 목록 조회 (읽기 전용)
    @Transactional(readOnly = true)
    public Page<Customer> getAllCustomers(int offset, int count) {
        Pageable pageable = PageRequest.of(offset, count);
        return customerRepository.findAll(pageable);
    }

    // 2. 단일 고객 상세 조회 (읽기 전용)
    @Transactional(readOnly = true)
    public Customer getCustomerById(String customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + customerId));
    }

    // 3. 신규 고객 등록 (생성) - [수정 포인트: 비밀번호 암호화 추가]
    @Transactional
    public Customer createCustomer(Customer customer) {
        if (customer.getCustomerId() == null || customer.getCustomerPassword() == null) {
            throw new IllegalArgumentException("Customer ID and Password must not be null");
        }
        if (customerRepository.existsById(customer.getCustomerId())) {
            throw new IllegalStateException("Customer ID already exists");
        }
        
        // ⭐ 평문 비밀번호를 BCrypt로 암호화해서 저장해야 나중에 로그인이 성공합니다!
        String encodedPassword = passwordEncoder.encode(customer.getCustomerPassword());
        customer.setCustomerPassword(encodedPassword);

        if (customer.getCustomerPoint() == 0.0) {
            customer.setCustomerPoint(1000000.0);
        }
        return customerRepository.save(customer);
    }

    // 4. 로그인
    @Transactional(readOnly = true)
    public CustomerLoginResponseDto login(CustomerLoginRequestDto requestDto) {
        // 1. 아이디 조회
        Customer customer = customerRepository.findById(requestDto.getCustomerId())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 아이디입니다."));

        // 2. 비밀번호 매칭 검증
        if (!passwordEncoder.matches(requestDto.getCustomerPassword(), customer.getCustomerPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        // 3. 로그인 성공 시 인증 토큰 생성 및 캐시(TokenStore)에 저장
        String accessToken = tokenStore.generateToken(customer.getCustomerId());

        // 4. 안전한 응답 DTO에 토큰 실어서 반환 (엔티티에 없는 이름 대신 아이디와 포인트 반환)
        return new CustomerLoginResponseDto(
                customer.getCustomerId(),
                customer.getCustomerPoint(), // 이름 대신 포인트나 필요한 정보 전달
                accessToken
        );
    }

    // 5. 고객 정보 수정 (수정)
    @Transactional
    public Customer updateCustomer(Customer customer) {
        if (!customerRepository.existsById(customer.getCustomerId())) {
            throw new RuntimeException("Customer not found");
        }
        return customerRepository.save(customer);
    }

    // 6. 고객 삭제 (삭제)
    @Transactional
    public void deleteCustomer(Customer customer) {
        if (!customerRepository.existsById(customer.getCustomerId())) {
            throw new RuntimeException("Customer not found");
        }
        customerRepository.delete(customer);
    }

    // 7. 상품 주문 (추후 Order 파트와 연동 시 확장)
    public void placeOrder(OrderRequestDto order) {
        // TODO: 팀원의 주문 관련 파트와 연동
    }

    // 8. 주문 취소 (추후 Order 파트와 연동 시 확장)
    public void cancelOrder(OrderRequestDto order) {
        // TODO: 팀원의 주문 취소 관련 파트와 연동
    }
}