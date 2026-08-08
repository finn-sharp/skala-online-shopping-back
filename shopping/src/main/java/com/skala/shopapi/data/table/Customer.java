/*
고객(Customer) 엔티티 클래스
작성자 : 정희중
날짜 : 2026-08-07

컬럼 : customerId, customerPassword, customerPoint

변경사항 : 초기작성(정희중)
         AllArgsConstructor 추가 및 Builder 패턴 적용(김재현, 08.08)
*/

package com.skala.shopapi.data.table;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "customers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer {

    public Customer(String customerId, double customerPoint) {
        this.customerId = customerId;
        this.customerPoint = customerPoint;
    }

    @Id
    private String customerId; // 고객 아이디

    @Column(nullable = false, unique = true, length = 20)
    private String customerPassword; // 비밀번호

    @Column(nullable = false)
    private double customerPoint; // 고객 포인트
}
