package com.ugts.brand.service.impl;

import com.ugts.brand.dto.request.CreateBrandRequest;
import com.ugts.brand.dto.response.BrandResponse;
import com.ugts.brand.entity.Brand;
import com.ugts.brand.mapper.BrandMapper;
import com.ugts.brand.repository.BrandRepository;
import com.ugts.brand.service.BrandService;
import com.ugts.exception.AppException;
import com.ugts.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BrandServiceImpl implements BrandService {
    BrandRepository brandRepository;

    BrandMapper brandMapper;

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public BrandResponse createBrand(CreateBrandRequest request) {
        // check existed
        if (brandRepository.findByName(request.getName()).isPresent()) {
            throw new AppException(ErrorCode.BRAND_EXISTED);
        }

        // create brand
        Brand brand = brandMapper.toBrand(request);

        // save to db
        brand = brandRepository.save(brand);

        return brandMapper.toBrandResponse(brand);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public List<BrandResponse> getAllBrands() {
        return brandRepository.findAll().stream()
                .map(brandMapper::toBrandResponse)
                .toList();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public BrandResponse getBrandByName(String name) {
        // check existed
        var brand = brandRepository.findByName(name)
                .orElseThrow(() -> new AppException(ErrorCode.BRAND_NOT_EXISTED));
        return brandMapper.toBrandResponse(brand);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public void deleteBrand(String name) {
        // check existed
        var brand = brandRepository.findByName(name)
                .orElseThrow(() -> new AppException(ErrorCode.BRAND_NOT_EXISTED));
        brandRepository.delete(brand);
    }
}
