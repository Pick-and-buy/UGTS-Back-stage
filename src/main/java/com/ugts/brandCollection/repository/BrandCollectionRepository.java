package com.ugts.brandCollection.repository;

import java.util.Optional;

import com.ugts.brand.entity.Brand;
import com.ugts.brandCollection.entity.BrandCollection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandCollectionRepository extends JpaRepository<BrandCollection, Long> {
    boolean existsByBrandAndCollectionName(Brand brand, String collectionName);

    Optional<BrandCollection> findByCollectionName(String collectionName);
}
