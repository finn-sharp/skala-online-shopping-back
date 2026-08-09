/*
고객(Customer) 엔티티 클래스
작성자 : 정희중
날짜 : 2026-08-07

컬럼 : customerId, customerPassword, customerPoint

변경사항 : 초기작성(정희중)
         AllArgsConstructor 추가 및 Builder 패턴 적용(김재현, 08.08)
         role 필드 추가(김재현, 08.09)
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

    @Id
    private String customerId; // 고객 아이디

    @Column(nullable = false, length = 100)
    private String customerPassword; // 암호화된 비밀번호

    @Column(nullable = false)
    private double customerPoint; // 고객 포인트

    @Column(nullable = false)
    private String role; // 권한 (예: "ROLE_ADMIN", "ROLE_USER")
}