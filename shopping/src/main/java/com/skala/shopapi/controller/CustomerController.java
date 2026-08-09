package com.skala.shopapi.controller;

import com.skala.shopapi.common.PagedList;
import com.skala.shopapi.common.Response;
import com.skala.shopapi.data.table.Customer;
import com.skala.shopapi.data.dto.CustomerLoginRequestDto;
import com.skala.shopapi.data.dto.CustomerLoginResponseDto;
import com.skala.shopapi.data.dto.OrderRequestDto;
import com.skala.shopapi.data.dto.OrderResponseDto;
import com.skala.shopapi.service.CustomerService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
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

@Tag(name = "Customer API", description = "고객 관리 및 주문 API")
@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping("/list")
    @Operation(summary = "전체 고객 목록 조회", description = "페이징을 적용하여 전체 고객 목록을 조회합니다.")
    public ResponseEntity<Response<PagedList<Customer>>> getAllCustomers(
        @RequestParam(value="offset", defaultValue="0") int offset,
        @RequestParam(value="count", defaultValue="10") int count){
            Page<Customer> customerPage = customerService.getAllCustomers(offset, count);
            PagedList<Customer> pagedList = PagedList.of(customerPage, offset, count);
            
            return ResponseEntity.ok(Response.success(pagedList));
    }
    
    @GetMapping("/{customerId}")
    @Operation(summary = "단일 고객 상세 조회", description = "고객 ID를 통해 특정 고객 정보 및 주문 상품 리스트를 조회합니다.")
    public ResponseEntity<Response<Customer>> getCustomerById(@PathVariable String customerId){
        Customer customer = customerService.getCustomerById(customerId);
        return ResponseEntity.ok(Response.success(customer));
    }

    @PostMapping
    @Operation(summary = "신규 고객 등록", description = "새로운 고객 정보를 DB에 등록합니다.")
    public ResponseEntity<Response<Customer>> createCustomer(
            @RequestBody @Valid Customer customer
    ){
        Customer createdCustomer = customerService.createCustomer(customer);
        return ResponseEntity.ok(Response.success(createdCustomer));
    }

    @PostMapping("/login")
    @Operation(summary = "고객 로그인", description = "ID/Password를 통해 서비스에 로그인합니다.")
    public ResponseEntity<Response<CustomerLoginResponseDto>> login(
            @RequestBody @Valid CustomerLoginRequestDto requestDto
    ) {
        CustomerLoginResponseDto response = customerService.login(requestDto);
        return ResponseEntity.ok(Response.success(response));
    }

    @PutMapping
    @Operation(summary = "고객 정보 수정", description = "고객 정보를 DB에 업데이트합니다.")
    public ResponseEntity<Response<Customer>> updateCustomer(
            @RequestBody @Valid Customer customer
    ){
        Customer updatedCustomer = customerService.updateCustomer(customer);
        return ResponseEntity.ok(Response.success(updatedCustomer));
    }

    @DeleteMapping("/{customerId}") 
    @Operation(summary = "고객 삭제", description = "고객 ID를 받아 탈퇴 처리합니다.")
    public ResponseEntity<Response<Void>> deleteCustomer(@PathVariable String customerId) {
        customerService.deleteCustomer(customerId);
        return ResponseEntity.ok(Response.success()); // 데이터가 없는 성공 응답 (Void)
    }
    
    @PostMapping("/order")
    @Operation(summary = "상품 주문", description = "고객이 원하는 상품과 수량을 주문하고 포인트를 차감합니다.")
    public ResponseEntity<Response<OrderResponseDto>> placeOrder(
            @RequestBody @Valid OrderRequestDto order
    ) {
        OrderResponseDto response = customerService.placeOrder(order);
        return ResponseEntity.ok(Response.success(response));
    }

    @PostMapping("/cancel")
    @Operation(summary = "주문 취소", description = "주문했던 상품의 수량을 취소하고 포인트를 환급받습니다.")
    public ResponseEntity<Response<OrderResponseDto>> cancelOrder(
            @RequestBody @Valid OrderRequestDto order
    ) {
        OrderResponseDto response = customerService.cancelOrder(order);
        return ResponseEntity.ok(Response.success(response));
    }
}