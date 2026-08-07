package com.skala.shopapi.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    // private final ProductService productService;

    // @GetMapping("/list")
    // public Response getAllProducts(
    //     @ResponseParam(dafaultValue="0") Integer offset,
    //     @RequestParam(defaultValue="10") Integer count){
    //         return 상품 목록 DTO(페이징 적용)
    //     }

    // @GetMapping("/{id}")
    // public Response getProductById(@PathVariable Long id){
    //     return 특정 ID 상품 정보 반환 DTO
    // }
}
