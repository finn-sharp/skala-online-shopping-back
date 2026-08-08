package com.skala.shopapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.skala.shopapi.data.table.Customer;
import com.skala.shopapi.data.table.OrderItem;
import com.skala.shopapi.data.table.Product;

import java.util.List;

public interface CustomerProductRepository extends JpaRepository<OrderItem, Long> {

    List<OrderItem> findByCustomer_CustomerId(Long customerId);

    List<OrderItem> findByCustomerAndProduct(Customer customer, Product product);

    List<OrderItem> findByCustomer(Customer customer);
    
}