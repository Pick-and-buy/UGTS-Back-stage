package com.ugts.brand.repository;

import com.ugts.brand.entity.Brand;
import com.ugts.brand.entity.BrandLine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandLineRepository extends JpaRepository<BrandLine, Long> {
    boolean existsByBrandAndLineName(Brand brand, String lineName);
}
