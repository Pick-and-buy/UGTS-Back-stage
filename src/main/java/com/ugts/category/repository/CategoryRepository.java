package com.ugts.category.repository;

import java.util.List;
import java.util.Optional;

import com.ugts.brandLine.entity.BrandLine;
import com.ugts.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CategoryRepository extends JpaRepository<Category, String> {

    @Query("SELECT c FROM Category c WHERE c.categoryName = :categoryName AND c.brandLine.id = :brandLineId")
    Optional<Category> findByCategoryNameAndBrandLineId(@Param("categoryName") String categoryName, @Param("brandLineId") Long brandLineId);

    Optional<Category> findByCategoryName(String categoryName);

    boolean existsByCategoryNameAndBrandLine(String categoryName, BrandLine brandLine);

    void deleteByCategoryName(String categoryName);

    @Query("SELECT c FROM Category c JOIN c.brandLine bl WHERE bl.lineName = :lineName")
    List<Category> findByLineName(String lineName);
}
