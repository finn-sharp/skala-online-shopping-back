package com.skala.shopapi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.skala.shopapi.data.table.Product;
// import com.skala.shopapi.data.dto.ProductDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    // private final ProductService productService;

    @GetMapping("/list")
    public ResponseEntity<String> getAllProducts(
        @RequestParam(defaultValue = "0") Integer offset,
        @RequestParam(defaultValue = "10") Integer count) {
            return ResponseEntity.ok("상품 목록 DTO(페이징 적용)");
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getProductById(@PathVariable Long id) {
            return ResponseEntity.ok("특정 ID 상품 정보 반환 DTO");
    }

    @PostMapping
    public ResponseEntity<String> createProduct(@RequestBody Product product){
        return ResponseEntity.ok("새 상품 등록 결과 반환 -> DB 상 등록");
    }

    @PutMapping
    public ResponseEntity<String> updateProduct(@RequestBody Product product){
        return ResponseEntity.ok("상품 정보 업데이트 -> DB 상 업데이트");
    }

    @DeleteMapping
    public ResponseEntity<String> deleteProduct(@RequestBody Product product){
        return ResponseEntity.ok("서비스 상품 정보 전달(id로 충분) -> DB 상 삭제");
    }
}