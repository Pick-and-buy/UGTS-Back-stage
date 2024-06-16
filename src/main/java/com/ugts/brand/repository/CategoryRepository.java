package com.ugts.brand.repository;

import java.util.List;
import java.util.Optional;

import com.ugts.brand.entity.BrandLine;
import com.ugts.brand.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CategoryRepository extends JpaRepository<Category, String> {
    Optional<Category> findByCategoryName(String categoryName);

    boolean existsByCategoryNameAndBrandLine(String categoryName, BrandLine brandLine);

    void deleteByCategoryName(String categoryName);

    @Query("SELECT c FROM Category c JOIN c.brandLine bl WHERE bl.lineName = :lineName")
    List<Category> findByLineName(String lineName);
}
