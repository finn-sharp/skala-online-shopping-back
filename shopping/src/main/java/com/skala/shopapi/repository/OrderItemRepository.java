package com.skala.shopapi.repository;

import com.skala.shopapi.data.table.Customer;
import com.skala.shopapi.data.table.OrderItem;
import com.skala.shopapi.data.table.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    // 특정 고객과 특정 상품에 해당하는 주문 항목을 삭제하는 쿼리 메서드
    void deleteByCustomerAndProduct(Customer customer, Product product);
}