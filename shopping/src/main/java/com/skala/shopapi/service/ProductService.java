/*=
상품 서비스
작성자 : 정희중
날짜 : 2026-08-08

컬럼 : id, productName, productPrice

2026-08-08
getProductById : 아이디로 상품 정보 반환 함수
getAllProducts : 전체 상품 목록 반환 함수
createProduct : 상품 등록 함수
updateProduct : 상품 정보 수정 함수
deleteProduct : 상품 삭제 함수
*/

package com.skala.shopapi.service;

import com.skala.shopapi.repository.ProductRepository;

import jakarta.transaction.Transactional;

import com.skala.shopapi.data.table.Product;
import com.skala.shopapi.data.dto.ProductDto;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final  ProductRepository productRepository;


    //개별 상품 조회
    public ProductDto getProductById(Long id)
    {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다: " + id));

        return convertToDto(product);
    }

    // 전체 상품 목록 조회 ( offset 행부터 count 만큼 )
    public List<ProductDto> getAllProducts(int offset, int count) {
        /*
        if (count <= 0 || offset < 0) {
            throw new ParameterException("offset", "count");
        }

        */

        Pageable pageable = PageRequest.of(offset, count, Sort.by("id").ascending());

        return productRepository.findAll(pageable)
                .map(this::convertToDto)
                .getContent();
    }

    // 상품 등록
    @Transactional
    public ProductDto createProduct(ProductDto productDto) {

        /* 
        // 1. 입력값 검증
        if (productDto.getProductName() == null || productDto.getProductName().isBlank()
                || productDto.getProductPrice() <= 0) {
            throw new ParameterException("productName", "productPrice");
        }

        // 2. 이름 중복 체크
        productRepository.findByProductName(productDto.getProductName())
                .ifPresent(p -> {
                    throw new ResponseException(Error.DATA_DUPLICATED);
                });
        */
        // 3. 신규 Product 생성 (ID는 0L로 세팅 → JPA가 저장 시 자동 생성)
        Product product = Product.builder()
                .id(0L)
                .productName(productDto.getProductName())
                .productPrice(productDto.getProductPrice())
                .build();

        // 4. 저장 후 Response 반환
        Product savedProduct = productRepository.save(product);

        return convertToDto(savedProduct);
    }

    // 상품 정보 수정
    @Transactional
    public ProductDto updateProduct(Long id, ProductDto productDto)
    {
        /* 
        // 1. 입력값 검증
        if (productDto.getProductName() == null || productDto.getProductName().isBlank()
                || productDto.getProductPrice() <= 0) {
            throw new ParameterException("productName", "productPrice");
        }

        // 2. 이름 중복 체크
        productRepository.findByProductName(productDto.getProductName())
        .ifPresent(p -> {
            throw new ResponseException(Error.DATA_DUPLICATED);
        });
        */

        // 3. 상품 조회
        Product product = productRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다: " + id));

        // 수정 함수
        product.setProductName(productDto.getProductName());
        product.setProductPrice(productDto.getProductPrice());

        Product updatedProduct = productRepository.save(product);

        return convertToDto(updatedProduct);
    }

    // 상품 삭제
    @Transactional
    public ProductDto deleteProduct(ProductDto productDto)
    {
        Product product = productRepository.findById(productDto.getId())
        .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다: " + productDto.getId()));

        productRepository.delete(product);
        return convertToDto(product);
    }

    private ProductDto convertToDto(Product product) {   
        return ProductDto.builder()
                .id(product.getId())
                .productName(product.getProductName())
                .productPrice(product.getProductPrice())
                .build();
    }
}
