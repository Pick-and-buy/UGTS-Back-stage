package com.ugts.brand.service.impl;

import java.io.IOException;
import java.util.List;

import com.ugts.brand.dto.request.NewsRequest;
import com.ugts.brand.dto.response.NewsResponse;
import com.ugts.brand.entity.News;
import com.ugts.brand.mapper.NewsMapper;
import com.ugts.brand.repository.BrandLineRepository;
import com.ugts.brand.repository.NewsRepository;
import com.ugts.brand.service.NewsService;
import com.ugts.cloudService.GoogleCloudStorageService;
import com.ugts.exception.AppException;
import com.ugts.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class NewsServiceImpl implements NewsService {

    BrandLineRepository brandLineRepository;

    NewsRepository newsRepository;

    NewsMapper newsMapper;

    GoogleCloudStorageService googleCloudStorageService;

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    @Override
    public NewsResponse createNews(NewsRequest request, MultipartFile file) throws IOException {
        var brandLine = brandLineRepository
                .findByLineName(request.getBrandLine().getLineName())
                .orElseThrow(() -> new AppException(ErrorCode.BRAND_LINE_NOT_EXISTED));

        var news = News.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .brandLine(brandLine)
                .subTitle1(request.getSubTitle1())
                .subContent1(request.getSubContent1())
                .subTitle2(request.getSubTitle2())
                .subContent2(request.getSubContent2())
                .subTitle3(request.getSubTitle3())
                .subContent3(request.getSubContent3())
                .build();

        String newsBannerUrl = googleCloudStorageService.uploadNewsBannerToGCS(file, news.getId());

        news.setBanner(newsBannerUrl);

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
    public NewsResponse updateNews(NewsRequest request, String newsId, MultipartFile file) throws IOException {
        var news = newsRepository.findById(newsId).orElseThrow(() -> new AppException(ErrorCode.NEWS_NOT_EXISTED));

        var brandLine = brandLineRepository
                .findByLineName(request.getBrandLine().getLineName())
                .orElseThrow(() -> new AppException(ErrorCode.BRAND_LINE_NOT_EXISTED));

        String newsBannerUrl = googleCloudStorageService.uploadNewsBannerToGCS(file, newsId);

        news.setBrandLine(brandLine);
        news.setBanner(newsBannerUrl);
        news.setTitle(request.getTitle());
        news.setContent(request.getContent());
        news.setSubTitle1(request.getSubTitle1());
        news.setSubContent1(request.getSubContent1());
        news.setSubTitle2(request.getSubTitle2());
        news.setSubContent2(request.getSubContent2());
        news.setSubTitle3(request.getSubTitle3());
        news.setSubContent3(request.getSubContent3());

        return newsMapper.toNewsResponse(newsRepository.save(news));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    @Override
    public void deleteNews(String newsId) {
        newsRepository.deleteById(newsId);
    }
}
