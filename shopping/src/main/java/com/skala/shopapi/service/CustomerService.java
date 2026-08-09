package com.skala.shopapi.service;

import com.skala.shopapi.config.TokenStore;
import com.skala.shopapi.data.dto.CustomerLoginRequestDto;
import com.skala.shopapi.data.dto.CustomerLoginResponseDto;
import com.skala.shopapi.data.dto.OrderItemDto;
import com.skala.shopapi.data.dto.OrderRequestDto;
import com.skala.shopapi.data.table.Customer;
import com.skala.shopapi.data.table.OrderItem;
import com.skala.shopapi.data.dto.OrderResponseDto;
import com.skala.shopapi.data.table.Product;
import com.skala.shopapi.repository.CustomerRepository;
import com.skala.shopapi.repository.OrderItemRepository;
import com.skala.shopapi.repository.ProductRepository;
import com.skala.shopapi.exception.Error; // java.lang.Error 와 이름이 겹치므로 반드시 명시적으로 import
import com.skala.shopapi.exception.ParameterException;
import com.skala.shopapi.exception.ResponseException;

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
    private final ProductRepository productRepository;         // 추가됨
    private final OrderItemRepository orderItemRepository;     // 추가됨
    private final PasswordEncoder passwordEncoder;
    private final TokenStore tokenStore;

    // 1. 전체 고객 목록 조회 (읽기 전용)
    @Transactional(readOnly = true)
    public Page<Customer> getAllCustomers(int offset, int count) {
        if (offset < 0 || count <= 0) {
            throw new ParameterException("offset", "count");
        }

        Pageable pageable = PageRequest.of(offset, count);
        return customerRepository.findAll(pageable);
    }

    // 2. 단일 고객 상세 조회 (읽기 전용)
    @Transactional(readOnly = true)
    public Customer getCustomerById(String customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new ResponseException(Error.NOT_FOUND));
    }

    // 3. 신규 고객 등록 (생성)
    @Transactional
    public Customer createCustomer(Customer customer) {
        if (customer.getCustomerId() == null || customer.getCustomerPassword() == null) {
            throw new ResponseException(Error.NOT_FOUND);
        }
        if (customerRepository.existsById(customer.getCustomerId())) {
            throw new ResponseException(Error.NOT_FOUND);
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
                        .orElseThrow(() -> new ResponseException(Error.NOT_FOUND));

        String inputPassword = requestDto.getCustomerPassword();
        String dbPassword = customer.getCustomerPassword();
        
        // 2. 비밀번호 검증 (BCrypt 검증 성공 OR 평문이 일치하는 경우 모두 허용)
        boolean isMatch = passwordEncoder.matches(inputPassword, dbPassword) 
                || inputPassword.equals(dbPassword);

        if (!isMatch) {
            throw new ParameterException("inputPassword", "dbPassword");
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
        // 1. 기존 고객이 존재하는지 확인 (throw 누락 해결)
        Customer existingCustomer = customerRepository.findById(customer.getCustomerId())
                .orElseThrow(() -> new ResponseException(Error.NOT_FOUND));

        // 2. 비밀번호가 변경되어 들어온 경우, 평문일 수 있으므로 다시 암호화 처리 (선택적이지만 안전)
        if (customer.getCustomerPassword() != null && !customer.getCustomerPassword().equals(existingCustomer.getCustomerPassword())) {
            String encodedPassword = passwordEncoder.encode(customer.getCustomerPassword());
            customer.setCustomerPassword(encodedPassword);
        }

        // 3. 수정된 정보 저장
        return customerRepository.save(customer);
    }

    // 6. 고객 삭제 (삭제)
    @Transactional
    public void deleteCustomer(String customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + customerId));
        
        customerRepository.delete(customer);
    }

    /**
     * 7. 상품 주문
     */
    @Transactional
    public OrderResponseDto placeOrder(OrderRequestDto orderRequest) {
        Customer customer = customerRepository.findById(orderRequest.getCustomerId())
                .orElseThrow(() -> new ResponseException(Error.NOT_FOUND));

        double totalAmount = 0.0;

        for (OrderItemDto itemDto : orderRequest.getOrderItems()) {
            Product product = productRepository.findById(itemDto.getProductId())
                .orElseThrow(() -> new ResponseException(Error.NOT_FOUND));

            totalAmount += product.getProductPrice() * itemDto.getQuantity();

            OrderItem orderItem = new OrderItem(customer, product);
            orderItem.setQuantity(itemDto.getQuantity());
            orderItemRepository.save(orderItem);
        }

        if (customer.getCustomerPoint() < totalAmount) {
            throw new ParameterException("totalAmount","customerPoint");
        }

        customer.setCustomerPoint(customer.getCustomerPoint() - totalAmount);

        // 주문 성공 후 응답 DTO 빌드 및 반환
        return OrderResponseDto.builder()
                .customerId(customer.getCustomerId())
                .remainingPoint(customer.getCustomerPoint())
                .orderItems(orderRequest.getOrderItems())
                .build();
    }

/**
     * 8. 주문 취소
     * - 주문했던 금액만큼 포인트를 환급하고, 해당 주문 항목들을 삭제 처리한 뒤 결과 응답을 반환합니다.
     */
    @Transactional
    public OrderResponseDto cancelOrder(OrderRequestDto orderRequest) {
        // 1. 고객 조회
        Customer customer = customerRepository.findById(orderRequest.getCustomerId())
                .orElseThrow(() -> new ResponseException(Error.NOT_FOUND));

        double refundAmount = 0.0;

        // 2. 주문 취소 대상 상품들의 환급금 계산 및 주문 내역 삭제 처리
        for (OrderItemDto itemDto : orderRequest.getOrderItems()) {
            Product product = productRepository.findById(itemDto.getProductId())
                .orElseThrow(() -> new ResponseException(Error.NOT_FOUND));

            refundAmount += product.getProductPrice() * itemDto.getQuantity();

            // 데이터베이스에서 해당 고객이 주문한 특정 상품 내역을 찾아 삭제
            orderItemRepository.deleteByCustomerAndProduct(customer, product);
        }

        // 3. 포인트 환급
        customer.setCustomerPoint(customer.getCustomerPoint() + refundAmount);

        // 4. 취소 후 정보가 담긴 응답 DTO 반환
        return OrderResponseDto.builder()
                .customerId(customer.getCustomerId())
                .remainingPoint(customer.getCustomerPoint())
                .orderItems(orderRequest.getOrderItems())
                .build();
    }
}