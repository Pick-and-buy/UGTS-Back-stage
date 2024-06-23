package com.ugts.brandCollection.controller;

import java.io.IOException;

import com.ugts.brandCollection.dto.request.BrandCollectionRequest;
import com.ugts.brandCollection.dto.response.BrandCollectionResponse;
import com.ugts.brandCollection.service.BrandCollectionService;
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
@RequestMapping("/api/v1/brand-collections")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BrandCollectionController {

    BrandCollectionService brandCollectionService;

    @PostMapping
    public ApiResponse<BrandCollectionResponse> createBrandCollection(
            @RequestPart BrandCollectionRequest request,
            @RequestPart("brandCollectionImages") MultipartFile[] brandCollectionImages)
            throws IOException {
        var result = brandCollectionService.createBrandCollection(request, brandCollectionImages);
        return ApiResponse.<BrandCollectionResponse>builder()
                .message("Create new brand collection success")
                .result(result)
                .build();
    }
}
