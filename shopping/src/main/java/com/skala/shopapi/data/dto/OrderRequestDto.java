package com.skala.shopapi.data.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
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

    @NotEmpty(message = "주문 상품 목록은 필수입니다")
    @Valid // List 내부의 OrderItemDto 필드들에 대한 @Valid 검증을 활성화합니다.
    private List<OrderItemDto> orderItems;
}