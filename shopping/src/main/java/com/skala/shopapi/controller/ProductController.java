/*

2026-08-09
Response 적용
@valid 적용
*/

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

import com.skala.shopapi.common.PagedList;
import com.skala.shopapi.common.Response;
import com.skala.shopapi.data.dto.ProductDto;
import com.skala.shopapi.service.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping("/list")
    @Operation(summary = "상품 목록 조회", description = "offset/count 기준으로 상품 목록을 조회합니다.")
    public ResponseEntity<Response<PagedList<ProductDto>>> getAllProducts(
        @RequestParam(defaultValue = "0") Integer offset,
        @RequestParam(defaultValue = "10") Integer count) {

            PagedList<ProductDto> products = productService.getAllProducts(offset, count);

            return ResponseEntity.ok(Response.success(products));
    }

    @GetMapping("/{id}")
    @Operation(summary = "특정 상품 정보 조회", description = "특정 상품 정보를 조회합니다.")
    public ResponseEntity<Response<ProductDto>> getProductById(@PathVariable("id") Long id) {
        ProductDto product = productService.getProductById(id);

        return ResponseEntity.ok(Response.success(product));
    }

    @PostMapping
    @Operation(summary = "신규 상품 등록", description = "신규 상품을 등록합니다.")
    public ResponseEntity<Response<ProductDto>> createProduct(@Valid @RequestBody ProductDto product){

        ProductDto productDto = productService.createProduct(product);
        return ResponseEntity.ok(Response.success(productDto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "특정 상품 정보 수정", description = "특정 상품 정보를 수정합니다.")
    public ResponseEntity<Response<ProductDto>> updateProduct(
        @PathVariable("id") Long id,
        @Valid @RequestBody ProductDto product){
        ProductDto productDto = productService.updateProduct(id,product);

        return ResponseEntity.ok(Response.success(productDto));
    }

    @DeleteMapping
    @Operation(summary = "특정 상품 정보 삭제", description = "특정 상품 정보를 삭제합니다.")
    public ResponseEntity<Response<ProductDto>> deleteProduct(@RequestBody ProductDto product){
        ProductDto productDto = productService.deleteProduct(product);
        return ResponseEntity.ok(Response.success(productDto));
    }
}