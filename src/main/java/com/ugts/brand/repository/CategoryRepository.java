package com.ugts.brand.repository;

import java.util.Optional;

import com.ugts.brand.entity.BrandLine;
import com.ugts.brand.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, String> {
    Optional<Category> findByCategoryName(String categoryName);

    boolean existsByCategoryNameAndBrandLine(String categoryName, BrandLine brandLine);

    void deleteByCategoryName(String categoryName);
}
