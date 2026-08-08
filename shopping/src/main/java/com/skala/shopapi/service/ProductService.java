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
import com.skala.shopapi.common.PagedList;
import com.skala.shopapi.data.dto.ProductDto;
import com.skala.shopapi.exception.Error; // java.lang.Error 와 이름이 겹치므로 반드시 명시적으로 import
import com.skala.shopapi.exception.ParameterException;
import com.skala.shopapi.exception.ResponseException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

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
                .orElseThrow(() -> new ResponseException(Error.NOT_FOUND));

        return convertToDto(product);
    }

    // 전체 상품 목록 조회 ( offset 행부터 count 만큼 )
    public PagedList<ProductDto> getAllProducts(int offset, int count) {
        if (offset < 0 || count <= 0) {
            throw new ParameterException("offset", "count");
        }

        Pageable pageable = PageRequest.of(offset / count, count, Sort.by("id").ascending());
        Page<ProductDto> page = productRepository.findAll(pageable).map(this::convertToDto);

        return PagedList.of(page, offset, count);
    } 

    // 상품 등록
    @Transactional
    public ProductDto createProduct(ProductDto productDto) {

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

        // 3. 신규 Product 생성 (id 를 넣지 않아야 JPA 가 신규로 판단해 자동 생성한다)
        Product product = Product.builder()
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
        // 1. 입력값 검증
        if (productDto.getProductName() == null || productDto.getProductName().isBlank()
                || productDto.getProductPrice() <= 0) {
            throw new ParameterException("productName", "productPrice");
        }

        // 2. 이름 중복 체크 (자기 자신은 중복이 아니므로 제외)
        productRepository.findByProductName(productDto.getProductName())
        .filter(p -> !p.getId().equals(id))
        .ifPresent(p -> {
            throw new ResponseException(Error.DATA_DUPLICATED);
        });

        // 3. 상품 조회
        Product product = productRepository.findById(id)
        .orElseThrow(() -> new ResponseException(Error.NOT_FOUND));

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
        // 1. 입력값 검증 (id 가 null 이면 findById 자체가 예외를 던지므로 먼저 막는다)
        if (productDto.getId() == null) {
            throw new ParameterException("id");
        }

        // 2. 상품 조회
        Product product = productRepository.findById(productDto.getId())
        .orElseThrow(() -> new ResponseException(Error.NOT_FOUND));

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
