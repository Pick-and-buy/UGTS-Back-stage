package com.ugts.product.repository;

import java.util.Optional;

import com.ugts.product.entity.ProductData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductDataRepository extends JpaRepository<ProductData, Integer> {
    @Query("SELECT p FROM ProductData p " + "WHERE LOWER(p.brand) = LOWER(:brand) "
            + "AND (:brandLine IS NULL OR LOWER(:brandLine) = 'không rõ' OR LOWER(p.brandLine) = LOWER(:brandLine)) "
            + "AND (:category IS NULL OR LOWER(:category) = 'không rõ' OR LOWER(p.category) = LOWER(:category)) "
            + "AND FUNCTION('similarity', LOWER(p.productName), LOWER(:productName)) > 0.7")
    Optional<ProductData> findSimilarProduct(
            @Param("brand") String brand,
            @Param("brandLine") String brandLine,
            @Param("category") String category,
            @Param("productName") String productName);
}
