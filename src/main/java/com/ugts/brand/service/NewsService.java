package com.ugts.brand.service;

import com.ugts.brand.dto.request.NewsRequest;
import com.ugts.brand.dto.response.NewsResponse;

import java.util.List;

public interface NewsService {
    NewsResponse createNews(NewsRequest request);

    List<NewsResponse> getAllNews();

    NewsResponse getNewsById(String newsId);

    NewsRequest updateNews(NewsRequest request, String newsId);

}
