package com.skala.shopapi.data.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerLoginResponseDto {
    
    private String customerId;
    
    private double customerPoint; 

    private String role;
    
    private String accessToken;
}