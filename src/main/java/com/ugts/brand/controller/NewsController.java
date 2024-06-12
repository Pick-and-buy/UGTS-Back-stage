package com.ugts.brand.controller;

import com.ugts.brand.dto.request.NewsRequest;
import com.ugts.brand.dto.response.NewsResponse;
import com.ugts.brand.service.NewsService;
import com.ugts.dto.ApiResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/news")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NewsController {

    NewsService newsService;

    @PostMapping
    public ApiResponse<NewsResponse> createNews(@RequestBody NewsRequest request) {
        var result = newsService.createNews(request);
        return ApiResponse.<NewsResponse>builder()
                .message("Create news success")
                .result(result)
                .build();
    }
}
