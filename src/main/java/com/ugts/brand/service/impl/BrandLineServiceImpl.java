package com.ugts.brand.service.impl;

import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import com.ugts.brand.dto.request.BrandLineRequest;
import com.ugts.brand.dto.response.BrandLineResponse;
import com.ugts.brand.entity.BrandLine;
import com.ugts.brand.entity.BrandLineImage;
import com.ugts.brand.mapper.BrandLineMapper;
import com.ugts.brand.repository.BrandLineRepository;
import com.ugts.brand.repository.BrandRepository;
import com.ugts.brand.service.BrandLineService;
import com.ugts.cloudService.GoogleCloudStorageService;
import com.ugts.exception.AppException;
import com.ugts.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BrandLineServiceImpl implements BrandLineService {

    BrandRepository brandRepository;

    BrandLineRepository brandLineRepository;

    GoogleCloudStorageService googleCloudStorageService;

    BrandLineMapper brandLineMapper;

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    @Override
    public BrandLineResponse createBrandLine(BrandLineRequest request, MultipartFile[] files) throws IOException {
        var brand = brandRepository
                .findByName(request.getBrandRequest().getName())
                .orElseThrow(() -> new AppException(ErrorCode.BRAND_NOT_EXISTED));

        if (brandLineRepository.existsByBrandAndLineName(brand, request.getLineName())) {
            throw new AppException(ErrorCode.BRAND_COLLECTION_EXISTED);
        }

        var brandLine = BrandLine.builder()
                .brand(brand)
                .lineName(request.getLineName())
                .description(request.getDescription())
                .launchDate(request.getLaunchDate())
                .signatureFeatures(request.getSignatureFeatures())
                .priceRange(request.getPriceRange())
                .availableStatus(request.getAvailableStatus())
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();

        var newBrandLine = brandLineRepository.save(brandLine);

        // upload brand line image to GCS
        List<String> fileUrls = googleCloudStorageService.uploadBrandLineImagesToGCS(files, newBrandLine.getId());

        // add brand line image for each URL
        for (String fileUrl : fileUrls) {
            // check if brand line image null
            if (brandLine.getBrandLineImages() == null) {
                brandLine.setBrandLineImages(new HashSet<>());
            }
            // add brand line images to brand line
            var brandLineImage = BrandLineImage.builder()
                    .brandLine(newBrandLine)
                    .lineImageUrl(fileUrl)
                    .build();
            brandLine.getBrandLineImages().add(brandLineImage);
        }
        brandLineRepository.save(brandLine);

        return brandLineMapper.toBrandLineResponse(brandLineRepository.save(newBrandLine));
    }

    @Override
    public List<BrandLineResponse> getBrandLines() {
        return List.of();
    }

    @Override
    public BrandLineResponse getBrandCollectionByCollectionName(String brandLineName) {
        return null;
    }
}
