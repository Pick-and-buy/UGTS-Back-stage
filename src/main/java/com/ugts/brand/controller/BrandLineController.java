package com.ugts.brand.controller;

import java.io.IOException;

import com.ugts.brand.dto.request.BrandLineRequest;
import com.ugts.brand.dto.response.BrandLineResponse;
import com.ugts.brand.service.BrandLineService;
import com.ugts.dto.ApiResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
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
}
