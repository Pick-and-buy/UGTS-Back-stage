package com.ugts.product.repository;

import java.util.Optional;

import com.ugts.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findById(String postId);
}
