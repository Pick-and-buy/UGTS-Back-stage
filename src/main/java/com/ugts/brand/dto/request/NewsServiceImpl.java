package com.ugts.brand.dto.request;

import java.util.List;

import com.ugts.brand.dto.response.NewsResponse;
import com.ugts.brand.entity.News;
import com.ugts.brand.mapper.NewsMapper;
import com.ugts.brand.repository.BrandLineRepository;
import com.ugts.brand.repository.NewsRepository;
import com.ugts.brand.service.NewsService;
import com.ugts.exception.AppException;
import com.ugts.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class NewsServiceImpl implements NewsService {

    BrandLineRepository brandLineRepository;

    NewsRepository newsRepository;

    NewsMapper newsMapper;

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    @Override
    public NewsResponse createNews(NewsRequest request) {
        var brandLine = brandLineRepository
                .findByLineName(request.getBrandLine().getLineName())
                .orElseThrow(() -> new AppException(ErrorCode.BRAND_LINE_NOT_EXISTED));

        var news = News.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .brandLine(brandLine)
                .build();

        return newsMapper.toNewsResponse(newsRepository.save(news));
    }

    @Override
    public List<NewsResponse> getAllNews() {
        return newsRepository.findAll().stream().map(newsMapper::toNewsResponse).toList();
    }

    @Override
    public NewsResponse getNewsById(String newsId) {
        var news = newsRepository.findById(newsId).orElseThrow(() -> new AppException(ErrorCode.NEWS_NOT_EXISTED));
        return newsMapper.toNewsResponse(news);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    @Override
    public NewsResponse updateNews(NewsRequest request, String newsId) {
        var news = newsRepository.findById(newsId).orElseThrow(() -> new AppException(ErrorCode.NEWS_NOT_EXISTED));

        var brandLine = brandLineRepository
                .findByLineName(request.getBrandLine().getLineName())
                .orElseThrow(() -> new AppException(ErrorCode.BRAND_LINE_NOT_EXISTED));

        news.setBrandLine(brandLine);
        news.setTitle(request.getTitle());
        news.setContent(request.getContent());

        return newsMapper.toNewsResponse(newsRepository.save(news));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    @Override
    public void deleteNews(String newsId) {
        newsRepository.deleteById(newsId);
    }
}
