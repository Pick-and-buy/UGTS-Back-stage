package com.ugts.brand.repository;

import com.ugts.brand.entity.Brand;
import com.ugts.brand.entity.BrandCollection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandCollectionRepository extends JpaRepository<BrandCollection, Long> {
    boolean existsByBrandAndCollectionName(Brand brand, String collectionName);
}
