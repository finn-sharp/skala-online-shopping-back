/*
상품 정보 응답 DTO 클래스
작성자 : 정희중
날짜 : 2026-08-08

컬럼 : id, productName, productPrice

2026-08-08
validation을 위해 NotNull, NotBlank 어노테이션을 사용하여 필수 항목을 지정
*/

package com.skala.shopapi.data.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDto {
 
    
    private Long id;

    @NotBlank(message = "상품명은 필수입니다")
    @NotNull(message = "상품명은 필수입니다")
    private String productName; // 물품 코드

    @Positive
    @NotNull(message = "상품명은 필수입니다")
    private double productPrice; // 물품 가격
}
