/*
고객 주문 상품 목록 조회 응담 DTO 클래스
작성자 : 정희중
날짜 : 2026-08-07

컬럼 : customerId, costomerPoint, orderItems

2026-08-07
validation을 위해 NotNull 어노테이션을 사용하여 필수 항목을 지정
*/

package com.skala.shopping.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.validation.constraints.NotNull;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class OrderListDto {
    
    @NotNull(message = "고객 ID는 필수입니다")
    private String customerId;

    @NotNull(message = "고객 포인트는 필수입니다")
    private Double costomerPoint;

    @NotNull(message = "주문 항목은 필수입니다")
    private List<OrderItemDto> orderItems;
}
