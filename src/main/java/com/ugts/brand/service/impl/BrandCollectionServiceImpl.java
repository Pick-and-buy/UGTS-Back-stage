package com.ugts.brand.service.impl;

import com.ugts.brand.dto.request.BrandCollectionRequest;
import com.ugts.brand.dto.response.BrandCollectionResponse;
import com.ugts.brand.entity.BrandCollection;
import com.ugts.brand.entity.BrandCollectionImage;
import com.ugts.brand.mapper.BrandCollectionMapper;
import com.ugts.brand.repository.BrandCollectionRepository;
import com.ugts.brand.repository.BrandRepository;
import com.ugts.brand.service.BrandCollectionService;
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

import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BrandCollectionServiceImpl implements BrandCollectionService {

    BrandRepository brandRepository;

    BrandCollectionRepository brandCollectionRepository;

    GoogleCloudStorageService googleCloudStorageService;

    BrandCollectionMapper brandCollectionMapper;

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    @Override
    public BrandCollectionResponse createBrandCollection(BrandCollectionRequest request, MultipartFile[] files) throws IOException {
        var brand = brandRepository.findByName(request.getBrandRequest().getName())
                .orElseThrow(() -> new AppException(ErrorCode.BRAND_NOT_EXISTED));

        if (brandCollectionRepository.existsByBrandAndCollectionName(brand, request.getCollectionName())) {
            throw new AppException(ErrorCode.BRAND_COLLECTION_EXISTED);
        }

        var brandCollection = BrandCollection.builder()
                .brand(brand)
                .collectionName(request.getCollectionName())
                .season(request.getSeason())
                .year(request.getYear())
                .theme(request.getTheme())
                .releaseDate(request.getReleaseDate())
                .endDate(request.getEndDate())
                .designer(request.getDesigner())
                .description(request.getDescription())
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();

        var newBrandCollection = brandCollectionRepository.save(brandCollection);

        // upload brand collection image to GCS
        List<String> fileUrls = googleCloudStorageService
                .uploadBrandCollectionImagesToGCS(files, newBrandCollection.getId());

        // add brand collection image for each URL
        for (String fileUrl : fileUrls) {
            // check if brand collection image null
            if (brandCollection.getBrandCollectionImages() == null) {
                brandCollection.setBrandCollectionImages(new HashSet<>());
            }
            // add brand collection image to brand collection
            var brandCollectionImage = BrandCollectionImage.builder()
                    .brandCollection(newBrandCollection)
                    .collectionImageUrl(fileUrl)
                    .build();
            brandCollection.getBrandCollectionImages().add(brandCollectionImage);
        }
        brandCollectionRepository.save(brandCollection);

        return brandCollectionMapper.toBrandCollectionResponse(brandCollectionRepository.save(newBrandCollection));
    }

    @Override
    public List<BrandCollectionResponse> getBrandCollections() {
        return List.of();
    }

    @Override
    public BrandCollectionResponse getBrandCollectionByCollectionName(String brandCollectionName) {
        return null;
    }
}
