package com.ugts.brand.service;

import com.ugts.brand.dto.request.CreateBrandRequest;
import com.ugts.brand.dto.response.BrandResponse;

import java.util.List;

public interface BrandService {
    BrandResponse createBrand(CreateBrandRequest request);

    List<BrandResponse> getAllBrands();

    BrandResponse getBrandByName(String name);

    void deleteBrand(String name);
}
