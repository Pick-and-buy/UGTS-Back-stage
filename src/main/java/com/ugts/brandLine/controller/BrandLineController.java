package com.ugts.brandLine.controller;

import java.io.IOException;
import java.util.List;

import com.ugts.brandLine.dto.request.BrandLineRequest;
import com.ugts.brandLine.dto.response.BrandLineResponse;
import com.ugts.brandLine.service.BrandLineService;
import com.ugts.dto.ApiResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/brand-lines")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BrandLineController {

    BrandLineService brandLineService;

    @PostMapping
    public ApiResponse<BrandLineResponse> createBrandCollection(
            @RequestPart BrandLineRequest request, @RequestPart("brandLineImages") MultipartFile[] brandLineImages)
            throws IOException {
        var result = brandLineService.createBrandLine(request, brandLineImages);
        return ApiResponse.<BrandLineResponse>builder()
                .message("Create new brand line success")
                .result(result)
                .build();
    }

    @GetMapping
    public ApiResponse<List<BrandLineResponse>> getAllBrandLines() {
        var result = brandLineService.getBrandLines();
        return ApiResponse.<List<BrandLineResponse>>builder()
                .message("Get all brand lines success")
                .result(result)
                .build();
    }

    @GetMapping("/line-name")
    public ApiResponse<BrandLineResponse> getBrandLineByLineName(@RequestParam String brandLineName) {
        var result = brandLineService.getBrandLineByLineName(brandLineName);
        return ApiResponse.<BrandLineResponse>builder()
                .message("Get brand line by line name success")
                .result(result)
                .build();
    }

    @GetMapping("/brand-name")
    public ApiResponse<List<BrandLineResponse>> getBrandLineByBrandName(@RequestParam String brandName) {
        var result = brandLineService.getBrandLineByBrandName(brandName);
        return ApiResponse.<List<BrandLineResponse>>builder()
                .message("Get brand line by brand name success")
                .result(result)
                .build();
    }

    @DeleteMapping
    public void deleteBrandLine(@RequestParam String brandLineName) {
        brandLineService.deleteBrandLine(brandLineName);
    }
}