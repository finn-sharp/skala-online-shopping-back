package com.skala.shopapi.service;

import com.skala.shopapi.data.dto.CustomerLoginRequestDto;
import com.skala.shopapi.data.dto.CustomerLoginResponseDto;
import com.skala.shopapi.data.dto.OrderRequestDto;
import com.skala.shopapi.data.table.Customer;
import com.skala.shopapi.repository.CustomerRepository;
import com.skala.shopapi.config.TokenStore;

import lombok.RequiredArgsConstructor;

import org.antlr.v4.runtime.atn.SemanticContext.OR;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCrypt;
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

    // 3. 신규 고객 등록 (생성)
    @Transactional
    public Customer createCustomer(Customer customer) {
        if (customer.getCustomerId() == null || customer.getCustomerPassword() == null) {
            throw new IllegalArgumentException("Customer ID and Password must not be null");
        }
        if (customerRepository.existsById(customer.getCustomerId())) {
            throw new IllegalStateException("Customer ID already exists");
        }
        
        // 평문 비밀번호를 BCrypt로 암호화
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

        // 변수 선언 추가
        String inputPassword = requestDto.getCustomerPassword();
        String dbPassword = customer.getCustomerPassword();
        
        // 2. 비밀번호 검증 (BCrypt 검증 성공 OR 평문이 일치하는 경우 모두 허용)
        boolean isMatch = passwordEncoder.matches(inputPassword, dbPassword) 
                || inputPassword.equals(dbPassword);

        if (!isMatch) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        // 3. 로그인 성공 시 인증 토큰 생성 (role 정보 포함)        
        String accessToken = tokenStore.generateToken(customer.getCustomerId(), customer.getRole());
                
        // 4. 응답 DTO에 role 포함해서 반환
        return CustomerLoginResponseDto.builder()
                .customerId(customer.getCustomerId())
                .customerPoint(customer.getCustomerPoint())
                .role(customer.getRole())
                .accessToken(accessToken)
                .build();
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