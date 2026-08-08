package com.skala.shopapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.skala.shopapi.data.table.Product;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long>{
    Optional<Product> findByProductName(String productName);
}
