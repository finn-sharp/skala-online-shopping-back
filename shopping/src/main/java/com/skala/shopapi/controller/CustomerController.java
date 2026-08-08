package com.skala.shopapi.controller;

import com.skala.shopapi.data.table.Customer;
import com.skala.shopapi.data.dto.CustomerLoginRequestDto;
import com.skala.shopapi.data.dto.CustomerLoginResponseDto;
import com.skala.shopapi.data.dto.OrderRequestDto;
import com.skala.shopapi.service.CustomerService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

@Tag(name = "Customer API", description = "고객 관리 및 주문 API")
@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping("/list")
    @Operation(summary = "전체 고객 목록 조회", description = "페이징을 적용하여 전체 고객 목록을 조회합니다.")
    public ResponseEntity<Page<Customer>> getAllCustomers(
        @RequestParam(value="offset", defaultValue="0") int offset,
        @RequestParam(value="count", defaultValue="10") int count){
            Page<Customer> customers = customerService.getAllCustomers(offset, count);
            return ResponseEntity.ok(customers);
    }

    // // 💡 [일반 유저용] 내 정보 조회 API
    // @GetMapping("/me")
    // public ResponseEntity<CustomerResponseDto> getMyInfo(Authentication authentication) {
    //     // SecurityContext에 들어있는 현재 로그인한 유저의 ID(customerId) 추출
    //     String currentCustomerId = authentication.getName();
        
    //     CustomerResponseDto response = customerService.getCustomerByToken(currentCustomerId);
    //     return ResponseEntity.ok(response);
    // }

    // // 💡 [관리자용] 특정 유저 상세 조회 API
    // @PreAuthorize("hasRole('ADMIN')") // 관리자만 접근 가능
    // @GetMapping("/{customerId}")
    // public ResponseEntity<Customer> getCustomerByIdForAdmin(@PathVariable("id") String customerId) {
    //     Customer customer = customerService.getCustomerById(customerId);
    //     return ResponseEntity.ok(customer);
    // }
    
    
    @GetMapping("/{customerId}")
    @Operation(summary = "단일 고객 상세 조회", description = "고객 ID를 통해 특정 고객 정보 및 주문 상품 리스트를 조회합니다.")
    public ResponseEntity<Customer> getCustomerById(@PathVariable String customerId){
        Customer customer = customerService.getCustomerById(customerId);
        return ResponseEntity.ok(customer);
    }

    @PostMapping
    @Operation(summary = "신규 고객 등록", description = "새로운 고객 정보를 DB에 등록합니다.")
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer){
        Customer createdCustomer = customerService.createCustomer(customer);
        return ResponseEntity.ok(createdCustomer);
    }

    @PostMapping("/login")
    @Operation(summary = "신규 고객 등록", description = "새로운 고객 정보를 DB에 등록합니다.")
    public ResponseEntity<CustomerLoginResponseDto> login(@RequestBody CustomerLoginRequestDto requestDto) {
        // 서비스에서 토큰이 포함된 DTO를 받아옴
        CustomerLoginResponseDto response = customerService.login(requestDto);
        
        // 200 OK와 함께 클라이언트로 응답 전달
        return ResponseEntity.ok(response);
    }

    @PutMapping
    @Operation(summary = "고객 정보 수정", description = "고객 정보를 DB에 업데이트합니다.")
    public ResponseEntity<Customer> updateCustomer(@RequestBody Customer customer){
        Customer updatedCustomer = customerService.updateCustomer(customer);
        return ResponseEntity.ok(updatedCustomer);
    }

    @DeleteMapping
    @Operation(summary = "고객 삭제", description = "고객 정보를 DB에서 제거합니다.")
    public ResponseEntity<String> deleteCustomer(@RequestBody Customer customer){
        customerService.deleteCustomer(customer);
        return ResponseEntity.ok("고객 정보 삭제 완료");
    }

    @PostMapping("/order")
    @Operation(summary = "상품 주문", description = "고객이 원하는 상품과 수량을 주문하고 포인트를 차감합니다.")
    public ResponseEntity<String> placeOrder(@RequestBody OrderRequestDto order){
        customerService.placeOrder(order);
        return ResponseEntity.ok("상품 주문 완료");
    }

    @PostMapping("/cancel")
    @Operation(summary = "주문 취소", description = "주문했던 상품의 수량을 취소하고 포인트를 환급받습니다.")
    public ResponseEntity<String> cancelOrder(@RequestBody OrderRequestDto order){
        customerService.cancelOrder(order);
        return ResponseEntity.ok("주문 취소 완료");
    }
}