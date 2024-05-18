package com.ugts.brand.controller;

import com.ugts.brand.dto.request.CreateBrandRequest;
import com.ugts.brand.dto.response.BrandResponse;
import com.ugts.brand.service.BrandService;
import com.ugts.dto.ApiResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/brands")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BrandController {
    BrandService brandService;

    @PostMapping()
    public ApiResponse<BrandResponse> createBrand(@RequestBody CreateBrandRequest request) {
        var newBrand = brandService.createBrand(request);
        return ApiResponse.<BrandResponse>builder()
                .result(newBrand)
                .build();
    }

    @GetMapping
    public ApiResponse<List<BrandResponse>> getAllBrands() {
        return ApiResponse.<List<BrandResponse>>builder()
                .result(brandService.getAllBrands())
                .build();
    }

    @GetMapping("/{name}")
    public ApiResponse<BrandResponse> getBrandByName(@PathVariable String name) {
        return ApiResponse.<BrandResponse>builder()
                .result(brandService.getBrandByName(name))
                .build();
    }

    @DeleteMapping("/{name}")
    public void deleteBrand(@PathVariable String name) {
        brandService.deleteBrand(name);
    }
}
