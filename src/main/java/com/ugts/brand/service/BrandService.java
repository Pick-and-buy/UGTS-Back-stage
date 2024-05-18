package com.ugts.brand.service;

import com.ugts.brand.dto.request.BrandRequest;
import com.ugts.brand.dto.response.BrandResponse;

import java.util.List;

public interface BrandService {
    BrandResponse createBrand(BrandRequest request);

    List<BrandResponse> getAllBrands();

    BrandResponse getBrandByName(String name);

    void deleteBrand(String name);

    BrandResponse updateBrand(String name, BrandRequest request);
}
