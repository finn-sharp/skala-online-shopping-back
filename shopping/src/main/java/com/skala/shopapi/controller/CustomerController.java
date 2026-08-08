package com.skala.shopapi.controller;

import com.skala.shopapi.data.table.Customer;
import com.skala.shopapi.data.dto.CustomerSessionDto;
import com.skala.shopapi.data.dto.OrderRequestDto;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
public class CustomerController {
    // private final CustomerService customerService;

    @GetMapping("/list")
    public ResponseEntity<String> getAllcustomers(
        @RequestParam(value="offset", defaultValue="0") int offset,
        @RequestParam(value="count", defaultValue="0") int count){
            return ResponseEntity.ok("페이징이 적용된 고객 목록 반환");
        }

    @GetMapping("/{customerId}")
    public ResponseEntity<String> getCustomerById(@PathVariable String customerId){
        return ResponseEntity.ok("특정 고객 정보 및 주문 상품 리스트 반환");
    }

    @PostMapping
    public ResponseEntity<String> createCustomer(@RequestBody Customer customer){
        return ResponseEntity.ok("신규 고객 등록 > DB 상 등록");
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginCustomer(@RequestBody CustomerSessionDto customerSessionDto){
        return ResponseEntity.ok("로그인 성공 시, 토큰/세션 부여 및 고객 정보 반환");
    }

    @PutMapping
    public ResponseEntity<String> updateCustomer(@RequestBody Customer customer){
        return ResponseEntity.ok("수정할 고객 정보 > DB 상 업데이트");
    }

    @DeleteMapping
    public ResponseEntity<String> deleteCustomer(@RequestBody Customer customer){
        return ResponseEntity.ok("고객 정보 > DB 상 고객 정보 제거");
    }

    @PostMapping("/order")
    public ResponseEntity<String> placeOrder(@RequestBody OrderRequestDto order){
        return ResponseEntity.ok("고객이 상품 주문");
    }

    @PostMapping("/cancel")
    public ResponseEntity<String> cancelOrder(@RequestBody OrderRequestDto order){
        return ResponseEntity.ok("서비스로 OrderRequest 전달");
    }

}