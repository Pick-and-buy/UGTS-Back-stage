package com.ugts.brand.dto.request;

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

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class NewsServiceImpl implements NewsService{

    BrandLineRepository brandLineRepository;

    NewsRepository newsRepository;

    NewsMapper newsMapper;

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    @Override
    public NewsResponse createNews(NewsRequest request) {
        var brandLine = brandLineRepository.findByLineName(request.getBrandLine().getLineName())
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
        return newsRepository.findAll().stream()
                .map(newsMapper::toNewsResponse)
                .toList();
    }

    @Override
    public NewsResponse getNewsById(String newsId) {
        return null;
    }

    @Override
    public NewsRequest updateNews(NewsRequest request, String newsId) {
        return null;
    }
}
