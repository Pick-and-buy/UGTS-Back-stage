package com.ugts.brand.controller;

import java.util.List;

import com.ugts.brand.dto.request.NewsRequest;
import com.ugts.brand.dto.response.NewsResponse;
import com.ugts.brand.service.NewsService;
import com.ugts.dto.ApiResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping
    public ApiResponse<List<NewsResponse>> getAllNews() {
        var result = newsService.getAllNews();
        return ApiResponse.<List<NewsResponse>>builder()
                .message("Get all news success")
                .result(result)
                .build();
    }

    @GetMapping("/{newsId}")
    public ApiResponse<NewsResponse> getNewsById(@PathVariable String newsId) {
        var result = newsService.getNewsById(newsId);
        return ApiResponse.<NewsResponse>builder()
                .message("Get news success")
                .result(result)
                .build();
    }

    @PutMapping
    public ApiResponse<NewsResponse> updateNews(@RequestBody NewsRequest request, @RequestParam String newsId) {
        var result = newsService.updateNews(request, newsId);
        return ApiResponse.<NewsResponse>builder()
                .message("Update news with id " + newsId + " success")
                .result(result)
                .build();
    }

    @DeleteMapping
    public void deleteNews(@RequestParam String newsId) {
        newsService.deleteNews(newsId);
    }
}
