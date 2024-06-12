package com.ugts.brand.service;

import java.io.IOException;
import java.util.List;

import com.ugts.brand.dto.request.BrandLineRequest;
import com.ugts.brand.dto.response.BrandLineResponse;
import org.springframework.web.multipart.MultipartFile;

public interface BrandLineService {
    BrandLineResponse createBrandLine(BrandLineRequest request, MultipartFile[] files) throws IOException;

    List<BrandLineResponse> getBrandLines();

    BrandLineResponse getBrandLineByLineName(String brandLineName);

    void deleteBrandLine(String brandLineName);
}
