package com.ugts.brand.service.impl;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;

import com.ugts.brand.dto.request.BrandRequest;
import com.ugts.brand.dto.response.BrandResponse;
import com.ugts.brand.entity.Brand;
import com.ugts.brand.entity.BrandLogo;
import com.ugts.brand.mapper.BrandMapper;
import com.ugts.brand.repository.BrandRepository;
import com.ugts.brand.service.BrandService;
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
public class BrandServiceImpl implements BrandService {
    BrandRepository brandRepository;

    BrandMapper brandMapper;

    GoogleCloudStorageService googleCloudStorageService;

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    @Override
    public BrandResponse createBrand(BrandRequest request, MultipartFile[] files) throws IOException {
        // check existed
        if (brandRepository.findByName(request.getName()).isPresent()) {
            throw new AppException(ErrorCode.BRAND_EXISTED);
        }

        // create brand
        var brand = brandMapper.toBrand(request);

        // save new brand to database
        var newBrand = brandRepository.save(brand);

        // upload brand logo to GCS
        List<String> fileUrls = googleCloudStorageService.uploadBrandLogosToGCS(files, String.valueOf(brand.getId()));

        // add brand logo for each URL
        for (String fileUrl : fileUrls) {
            // check if brand image null
            if (newBrand.getBrandLogos() == null) {
                newBrand.setBrandLogos(new HashSet<>());
            }

            // add brand image to product
            var brandLogo = BrandLogo.builder().brand(newBrand).logoUrl(fileUrl).build();
            newBrand.getBrandLogos().add(brandLogo);
        }
        // save brand with logo to database
        brand = brandRepository.save(newBrand);

        return brandMapper.toBrandResponse(brand);
    }

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
        var brand = brandRepository.findByName(name).orElseThrow(() -> new AppException(ErrorCode.BRAND_NOT_EXISTED));
        return brandMapper.toBrandResponse(brand);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public void deleteBrand(String name) {
        // check existed
        var brand = brandRepository.findByName(name).orElseThrow(() -> new AppException(ErrorCode.BRAND_NOT_EXISTED));
        brandRepository.delete(brand);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public BrandResponse updateBrand(String name, BrandRequest request) {
        // update brand
        var brand = brandRepository
                .findByName(name)
                .map(existingBrand -> {
                    existingBrand.setName(request.getName());
                    return brandRepository.save(existingBrand);
                })
                .orElseThrow(() -> new AppException(ErrorCode.BRAND_NOT_EXISTED));

        return brandMapper.toBrandResponse(brand);
    }
}
