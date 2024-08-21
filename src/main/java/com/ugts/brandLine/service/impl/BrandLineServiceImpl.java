package com.ugts.brandLine.service.impl;

import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import com.ugts.brand.repository.BrandRepository;
import com.ugts.brandLine.dto.request.BrandLineRequest;
import com.ugts.brandLine.dto.response.BrandLineResponse;
import com.ugts.brandLine.entity.BrandLine;
import com.ugts.brandLine.entity.BrandLineImage;
import com.ugts.brandLine.mapper.BrandLineMapper;
import com.ugts.brandLine.repository.BrandLineRepository;
import com.ugts.brandLine.service.BrandLineService;
import com.ugts.common.cloudService.GoogleCloudStorageService;
import com.ugts.common.exception.AppException;
import com.ugts.common.exception.ErrorCode;
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

    /**
     * Creates a new brand line with the given request and images, uploading them to Google Cloud Storage.
     *
     * @param  request        the request containing the brand line details
     * @param  files          the images to be uploaded for the brand line
     * @return                the created brand line response
     * @throws IOException    if there is an error during the file upload
     */
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    @Override
    public BrandLineResponse createBrandLine(BrandLineRequest request, MultipartFile[] files) throws IOException {
        var brand = brandRepository
                .findByName(request.getBrandRequest().getName())
                .orElseThrow(() -> new AppException(ErrorCode.BRAND_NOT_EXISTED));

        if (brandLineRepository.existsByBrandAndLineName(brand, request.getLineName())) {
            throw new AppException(ErrorCode.BRAND_Line_ALREADY_EXISTED);
        }

        var brandLine = BrandLine.builder()
                .brand(brand)
                .lineName(request.getLineName())
                .description(request.getDescription())
                .launchDate(request.getLaunchDate())
                .signatureFeatures(request.getSignatureFeatures())
                .priceRange(request.getPriceRange())
                .availableStatus(true)
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();

        var newBrandLine = brandLineRepository.save(brandLine);

        // upload brand line image to GCS
        List<String> fileUrls = googleCloudStorageService.uploadBrandLineImagesToGCS(files, brandLine.getId());

        // add brand line image for each URL
        for (String fileUrl : fileUrls) {
            // check if brand line image null
            if (brandLine.getBrandLineImages() == null) {
                brandLine.setBrandLineImages(new HashSet<>());
            }
            // add brand line images to brand line
            var brandLineImage = BrandLineImage.builder()
                    .brandLine(brandLine)
                    .lineImageUrl(fileUrl)
                    .build();
            brandLine.getBrandLineImages().add(brandLineImage);
        }

        brandLineRepository.save(brandLine);

        return brandLineMapper.toBrandLineResponse(brandLineRepository.save(newBrandLine));
    }

    /**
     * Retrieves all brand lines and maps them to BrandLineResponse objects.
     *
     * @return          A list of BrandLineResponse objects representing all brand lines.
     */
    @Override
    public List<BrandLineResponse> getBrandLines() {
        return brandLineRepository.findAll().stream()
                .map(brandLineMapper::toBrandLineResponse)
                .toList();
    }

    /**
     * Retrieves a brand line based on the provided brand line name.
     *
     * @param  brandLineName  the name of the brand line to retrieve
     * @return          the response containing the brand line information
     */
    @Override
    public BrandLineResponse getBrandLineByLineName(String brandLineName) {
        var brandLine = brandLineRepository
                .findByLineName(brandLineName)
                .orElseThrow(() -> new AppException(ErrorCode.BRAND_LINE_NOT_EXISTED));
        return brandLineMapper.toBrandLineResponse(brandLine);
    }

    /**
     * Deletes a brand line by its name if the user has the 'ADMIN' role.
     *
     * @param  brandLineName  the name of the brand line to delete
     * @throws AppException    if the brand line does not exist
     */
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    @Override
    public void deleteBrandLine(String brandLineName) {
        var brandLine = brandLineRepository
                .findByLineName(brandLineName)
                .orElseThrow(() -> new AppException(ErrorCode.BRAND_LINE_NOT_EXISTED));
        brandLineRepository.delete(brandLine);
    }

    /**
     * Retrieves a list of BrandLineResponse objects based on the provided brand name.
     *
     * @param  brandName  the name of the brand to search for
     * @return            a list of BrandLineResponse objects corresponding to the brand name
     */
    @Override
    public List<BrandLineResponse> getBrandLineByBrandName(String brandName) {
        if (brandName == null || brandName.isEmpty()) {
            throw new IllegalArgumentException("Brand name cannot be null or empty");
        }
        return brandLineRepository.findBrandLinesByBrandName(brandName).stream()
                .map(brandLineMapper::toBrandLineResponse)
                .toList();
    }

    /**
     * Updates the brand line information for a given brand line name.
     *
     * @param  brandLineName  the name of the brand line to update
     * @param  request        the request containing the updated brand line information
     * @return                the updated brand line response
     * @throws AppException  if the brand line already exists or if the brand or brand line does not exist
     */
    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public BrandLineResponse updateBrandLineInformation(String brandLineName, BrandLineRequest request) {
        var existingBrandLine = brandLineRepository.findByLineName(request.getLineName());

        // check if brand line already existed
        if (existingBrandLine.isPresent()
                && !existingBrandLine.get().getLineName().equals(brandLineName)) {
            throw new AppException(ErrorCode.BRAND_Line_ALREADY_EXISTED);
        }

        var brand = brandRepository
                .findByName(request.getBrandRequest().getName())
                .orElseThrow(() -> new AppException(ErrorCode.BRAND_NOT_EXISTED));

        var brandLine = brandLineRepository
                .findByLineName(brandLineName)
                .orElseThrow(() -> new AppException(ErrorCode.BRAND_LINE_NOT_EXISTED));

        brandLine.setBrand(brand);
        brandLine.setLineName(request.getLineName());
        brandLine.setDescription(request.getDescription());
        brandLine.setLaunchDate(request.getLaunchDate());
        brandLine.setSignatureFeatures(request.getSignatureFeatures());
        brandLine.setPriceRange(request.getPriceRange());
        brandLine.setUpdatedAt(new Date());

        return brandLineMapper.toBrandLineResponse(brandLineRepository.save(brandLine));
    }
}
