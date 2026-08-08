/*
상품(Product) 엔티티 클래스
작성자 : 정희중
날짜 : 2026-08-07

컬럼 : id, productName, productPrice
*/

package com.skala.shopapi.data.table;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    public Product(String productName, double productPrice) {
        this.productName = productName;
        this.productPrice = productPrice;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String productName; // 물품 코드

    @Column(nullable = false)
    private double productPrice; // 물품 가격
}
