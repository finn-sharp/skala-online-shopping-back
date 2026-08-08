/*
주문 항목(OrderItem) 엔티티 클래스
작성자 : 정희중
날짜 : 2026-08-07

컬럼 : id, customer, product, quantity
*/

package com.skala.shopapi.data.table;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "order_items")
@Getter
@Setter
@NoArgsConstructor
public class OrderItem {

    public OrderItem(Customer customer, Product product) {
    
        this.customer = customer;
        this.product = product;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    // 주문한 고객과 상품을 나타내는 필드
    // 고객 한 명이 주문 항목을 여러개 만들 수 있기 때문에
    // ManyToOne 관계를 사용하고, 주문 항목은 고객과 상품에 종속되므로
    // fetch = FetchType.LAZY를 사용하여 지연 로딩을 설정.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer", nullable = false)
    private Customer customer;

    // 주문한 상품과 수량을 나타내는 필드
    // 상품 한 개가 여러 주문 항목에 포함될 수 있기 때문에
    // ManyToOne 관계를 사용하고, 주문 항목은 상품에 종속되므로
    // fetch = FetchType.LAZY를 사용하여 지연 로딩을 설정.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product", nullable = false)
    private Product product;

    @Column(nullable = false)
    private int quantity; // 주문 수량
}
