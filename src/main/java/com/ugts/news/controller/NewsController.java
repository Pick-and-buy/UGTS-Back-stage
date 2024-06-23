package com.ugts.news.controller;

import java.io.IOException;
import java.util.List;

import com.ugts.dto.ApiResponse;
import com.ugts.news.dto.request.NewsRequest;
import com.ugts.news.dto.response.NewsResponse;
import com.ugts.news.service.NewsService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/news")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NewsController {

    NewsService newsService;

    @PostMapping
    public ApiResponse<NewsResponse> createNews(@RequestPart NewsRequest request, @RequestPart MultipartFile banner)
            throws IOException {
        var result = newsService.createNews(request, banner);
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
    public ApiResponse<NewsResponse> updateNews(
            @RequestPart NewsRequest request, @RequestParam String newsId, @RequestPart MultipartFile banner)
            throws IOException {
        var result = newsService.updateNews(request, newsId, banner);
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
