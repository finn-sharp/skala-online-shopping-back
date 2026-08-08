package com.skala.shopapi.data.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerLoginRequestDto {

    @NotBlank(message = "고객 아이디는 필수 입력 값입니다.")
    private String customerId;

    @NotBlank(message = "고객 비밀번호는 필수 입력 값입니다.")
    private String customerPassword;
}

    