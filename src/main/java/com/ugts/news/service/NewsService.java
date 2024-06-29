package com.ugts.news.service;

import java.io.IOException;
import java.util.List;

import com.ugts.news.dto.request.NewsRequest;
import com.ugts.news.dto.response.NewsResponse;
import org.springframework.web.multipart.MultipartFile;

public interface NewsService {
    NewsResponse createNews(NewsRequest request, MultipartFile file) throws IOException;

    List<NewsResponse> getAllNews();

    NewsResponse getNewsById(String newsId);

    NewsResponse updateNews(NewsRequest request, String newsId, MultipartFile file) throws IOException;

    void deleteNews(String newsId);
}
