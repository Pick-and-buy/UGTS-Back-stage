package com.ugts.brand.service;

import java.util.List;

import com.ugts.brand.dto.request.NewsRequest;
import com.ugts.brand.dto.response.NewsResponse;

public interface NewsService {
    NewsResponse createNews(NewsRequest request);

    List<NewsResponse> getAllNews();

    NewsResponse getNewsById(String newsId);

    NewsResponse updateNews(NewsRequest request, String newsId);

    void deleteNews(String newsId);
}
