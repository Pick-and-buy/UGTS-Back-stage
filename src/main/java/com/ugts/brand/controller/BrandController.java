package com.ugts.brand.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ugts.brand.dto.request.BrandRequest;
import com.ugts.brand.dto.response.BrandResponse;
import com.ugts.brand.entity.Brand;
import com.ugts.brand.mapper.BrandMapper;
import com.ugts.brand.repository.BrandRepository;
import com.ugts.brand.service.BrandService;
import com.ugts.common.dto.ApiResponse;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/brands")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BrandController {
    BrandService brandService;

    ObjectMapper objectMapper;
    private final BrandRepository brandRepository;
    private final BrandMapper brandMapper;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<BrandResponse> createBrand(
            @RequestPart("request") String requestJson, @RequestPart("brandLogo") MultipartFile[] brandLogo)
            throws IOException {
        BrandRequest request = objectMapper.readValue(requestJson, BrandRequest.class);
        var newBrand = brandService.createBrand(request, brandLogo);
        return ApiResponse.<BrandResponse>builder().result(newBrand).build();
    }

    @GetMapping
    public ApiResponse<Map<String, Object>> getAllBrands(
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable paging = PageRequest.of(page, size);

            Page<Brand> pageBrands = brandRepository.findAll(paging);
            List<BrandResponse> brands = pageBrands.getContent().stream()
                    .map(brandMapper::toBrandResponse)
                    .collect(Collectors.toList());

            Map<String, Object> response = new HashMap<>();
            response.put("brands", brands);
            response.put("currentPage", pageBrands.getNumber());
            response.put("totalItems", pageBrands.getTotalElements());
            response.put("totalPages", pageBrands.getTotalPages());

            return ApiResponse.<Map<String, Object>>builder().result(response).build();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get brands", e);
        }
    }

    @GetMapping("/{name}")
    public ApiResponse<BrandResponse> getBrandByName(@NonNull @PathVariable String name) {
        return ApiResponse.<BrandResponse>builder()
                .result(brandService.getBrandByName(name))
                .build();
    }

    @DeleteMapping("/name")
    public void deleteBrand(@RequestParam String brandName) {
        brandService.deleteBrand(brandName);
    }

    @PutMapping("/name")
    public ApiResponse<BrandResponse> updateBrand(@RequestParam String brandName, @RequestBody BrandRequest request) {
        return ApiResponse.<BrandResponse>builder()
                .message("Update Brand Success")
                .result(brandService.updateBrand(brandName, request))
                .build();
    }
}
