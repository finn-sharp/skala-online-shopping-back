/*
주문 항목(OrderItem) DTO 클래스
작성자 : 정희중
날짜 : 2026-08-07
컬럼 : productId, productName, productPrice, quantity

2026-08-07
validation을 위해 NotNull 어노테이션을 사용하여 필수 항목을 지정
*/

package com.skala.shopping.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class OrderItemDto {

    @NotNull(message = "종목 코드는 필수입니다")
    private Long productId;

    @NotNull(message = "수량은 필수입니다")
    private String productName;

    @NotNull(message = "수량은 필수입니다")
    private Double productPrice;

    @NotNull(message = "수량은 필수입니다")
    private Integer quantity;
}
