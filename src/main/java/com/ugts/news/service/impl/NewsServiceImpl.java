package com.ugts.news.service.impl;

import java.io.IOException;
import java.util.List;

import com.ugts.brandLine.repository.BrandLineRepository;
import com.ugts.common.cloudService.GoogleCloudStorageService;
import com.ugts.common.exception.AppException;
import com.ugts.common.exception.ErrorCode;
import com.ugts.news.dto.request.NewsRequest;
import com.ugts.news.dto.response.NewsResponse;
import com.ugts.news.entity.News;
import com.ugts.news.mapper.NewsMapper;
import com.ugts.news.repository.NewsRepository;
import com.ugts.news.service.NewsService;
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
                .subContent2(request.getSubContent2())
                .subContent3(request.getSubContent3())
                .subContent4(request.getSubContent4())
                .subTitle2(request.getSubTitle2())
                .subContent5(request.getSubContent5())
                .subContent6(request.getSubContent6())
                .subContent7(request.getSubContent7())
                .subTitle3(request.getSubTitle3())
                .subContent8(request.getSubContent8())
                .subContent9(request.getSubContent9())
                .subContent10(request.getSubContent10())
                .build();

        String newsBannerUrl = googleCloudStorageService.uploadNewsBannerToGCS(file, news.getId());

        news.setBanner(newsBannerUrl);

        return newsMapper.toNewsResponse(newsRepository.save(news));
    }

    @Override
    public List<NewsResponse> getAllNews() {
        var news = newsRepository.findAll();
        return newsMapper.toAllNewsResponse(news);
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
        news.setSubContent2(request.getSubContent2());
        news.setSubContent3(request.getSubContent3());
        news.setSubContent4(request.getSubContent4());
        news.setSubTitle2(request.getSubTitle2());
        news.setSubContent5(request.getSubContent5());
        news.setSubContent6(request.getSubContent6());
        news.setSubContent7(request.getSubContent7());
        news.setSubTitle3(request.getSubTitle3());
        news.setSubContent8(request.getSubContent8());
        news.setSubContent9(request.getSubContent9());
        news.setSubContent10(request.getSubContent10());

        return newsMapper.toNewsResponse(newsRepository.save(news));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    @Override
    public void deleteNews(String newsId) {
        newsRepository.deleteById(newsId);
    }
}
