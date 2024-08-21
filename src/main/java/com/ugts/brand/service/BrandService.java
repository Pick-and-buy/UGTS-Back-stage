package com.ugts.brand.service;

import java.io.IOException;
import java.util.List;

import com.ugts.brand.dto.request.BrandRequest;
import com.ugts.brand.dto.response.BrandResponse;
import org.springframework.web.multipart.MultipartFile;

public interface BrandService {
    BrandResponse createBrand(BrandRequest request, MultipartFile[] files) throws IOException;

    List<BrandResponse> getAllBrands();

    BrandResponse getBrandByName(String name);

    void deleteBrand(String name);

    BrandResponse updateBrand(String name, BrandRequest request);
}
