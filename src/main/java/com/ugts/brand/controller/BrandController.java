package com.ugts.brand.controller;

import java.io.IOException;
import java.util.List;

import com.ugts.brand.dto.request.BrandRequest;
import com.ugts.brand.dto.response.BrandResponse;
import com.ugts.brand.service.BrandService;
import com.ugts.dto.ApiResponse;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/brands")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BrandController {
    BrandService brandService;

    @PostMapping()
    public ApiResponse<BrandResponse> createBrand(
            @NonNull @RequestPart BrandRequest request,
            @NonNull @RequestPart("brandLogo") MultipartFile[] brandLogo
    ) throws IOException {
        var newBrand = brandService.createBrand(request, brandLogo);
        return ApiResponse.<BrandResponse>builder().result(newBrand).build();
    }

    @GetMapping
    public ApiResponse<List<BrandResponse>> getAllBrands() {
        return ApiResponse.<List<BrandResponse>>builder()
                .result(brandService.getAllBrands())
                .build();
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
