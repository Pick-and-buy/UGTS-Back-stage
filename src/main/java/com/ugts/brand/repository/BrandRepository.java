package com.ugts.brand.repository;

import java.util.Optional;

import com.ugts.brand.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandRepository extends JpaRepository<Brand, Long> {
    Optional<Brand> findByName(String name);

    boolean existsByName(String name);
}
