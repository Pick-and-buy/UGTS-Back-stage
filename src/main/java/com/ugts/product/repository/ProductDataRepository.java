package com.ugts.product.repository;

import java.util.Optional;

import com.ugts.product.entity.ProductData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductDataRepository extends JpaRepository<ProductData, Integer> {
    @Query("SELECT p FROM ProductData p " + "WHERE LOWER(p.productName) ILIKE CONCAT('%', :productName, '%')"
            + "AND LOWER(p.brand) ILIKE CONCAT('%', :brand, '%')"
            + "AND LOWER(:brandLine) = 'Không rõ'  OR LOWER(p.brandLine) ILIKE CONCAT('%', :brandLine, '%')"
            + "AND LOWER(:category) = 'Không rõ' OR LOWER(p.category) ILIKE CONCAT('%', :category, '%')")
    Optional<ProductData> findSimilarProduct(
            @Param("productName") String productName,
            @Param("brand") String brand,
            @Param("brandLine") String brandLine,
            @Param("category") String category);
}
