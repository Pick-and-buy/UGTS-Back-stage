package com.ugts.brand.repository;

import java.util.Optional;

import com.ugts.brand.entity.Brand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandRepository extends JpaRepository<Brand, Long> {
    Optional<Brand> findByName(String name);

    Page<Brand> findAll(Pageable pageable);
}
