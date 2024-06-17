package com.ugts.brand.repository;

import java.util.List;
import java.util.Optional;

import com.ugts.brand.entity.Brand;
import com.ugts.brand.entity.BrandLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BrandLineRepository extends JpaRepository<BrandLine, Long> {
    boolean existsByBrandAndLineName(Brand brand, String lineName);

    Optional<BrandLine> findByLineName(String lineName);

    @Query("SELECT bl FROM BrandLine bl JOIN bl.brand b WHERE b.name = :brandName")
    List<BrandLine> findBrandLinesByBrandName(@Param("brandName") String brandName);
}
