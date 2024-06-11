package com.ugts.brand.service;

import java.io.IOException;
import java.util.List;

import com.ugts.brand.dto.request.BrandCollectionRequest;
import com.ugts.brand.dto.response.BrandCollectionResponse;
import org.springframework.web.multipart.MultipartFile;

public interface BrandCollectionService {
    BrandCollectionResponse createBrandCollection(BrandCollectionRequest request, MultipartFile[] files)
            throws IOException;

    List<BrandCollectionResponse> getBrandCollections();

    BrandCollectionResponse getBrandCollectionByCollectionName(String brandCollectionName);
}
