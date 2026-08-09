package com.skala.shopapi.data.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class OrderRequestDto {

    @NotNull(message = "고객 ID는 필수입니다")
    private String customerId;

    @NotNull(message = "주문 상품 목록은 필수입니다")
    private List<OrderItemDto> orderItems;
}