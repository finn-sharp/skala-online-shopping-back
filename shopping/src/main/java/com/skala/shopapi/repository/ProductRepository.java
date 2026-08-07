package com.skala.shopapi.repository;

import com.skala.shopapi.data.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ProductRepository extends JpaRepository{
    Optional<Product> findByProductName(String productName);
}
