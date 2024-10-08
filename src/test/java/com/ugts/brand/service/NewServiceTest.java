package com.ugts.brand.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import com.ugts.brandLine.entity.BrandLine;
import com.ugts.brandLine.repository.BrandLineRepository;
import com.ugts.common.cloudService.GoogleCloudStorageService;
import com.ugts.common.exception.AppException;
import com.ugts.common.exception.ErrorCode;
import com.ugts.news.dto.request.NewsRequest;
import com.ugts.news.dto.response.NewsResponse;
import com.ugts.news.entity.News;
import com.ugts.news.mapper.NewsMapper;
import com.ugts.news.repository.NewsRepository;
import com.ugts.news.service.impl.NewsServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.multipart.MultipartFile;

@SpringBootTest
@TestPropertySource("/test.properties")
public class NewServiceTest {
    @Mock
    private BrandLineRepository brandLineRepository;

    @Mock
    private NewsRepository newsRepository;

    @Mock
    private NewsMapper newsMapper;

    @Mock
    private GoogleCloudStorageService googleCloudStorageService;

    @InjectMocks
    private NewsServiceImpl newsService;

    @Mock
    private MultipartFile file;

    @Test
    public void createNews_success() throws IOException {
        NewsRequest request = new NewsRequest();
        request.setTitle("Test Title");
        request.setContent("Test Content");
        BrandLine brandLine = new BrandLine();
        brandLine.setLineName("Test Brand Line");
        request.setBrandLine(brandLine);

        News news = News.builder()
                .id("1")
                .title(request.getTitle())
                .content(request.getContent())
                .brandLine(brandLine)
                .build();
        when(brandLineRepository.findByLineName(anyString())).thenReturn(Optional.of(brandLine));
        when(newsRepository.save(any(News.class))).thenReturn(news);
        when(googleCloudStorageService.uploadNewsBannerToGCS(any(MultipartFile.class), anyString()))
                .thenReturn("Test URL");
        when(newsMapper.toNewsResponse(any(News.class))).thenReturn(new NewsResponse());

        NewsResponse response = newsService.createNews(request, file);

        assertNotNull(response);
        verify(newsRepository, times(1)).save(any(News.class));
    }

    @Test
    void createNews_BrandLineNotFound_fail() throws IOException {
        NewsRequest request = new NewsRequest();
        request.setTitle("Test Title");
        request.setContent("Test Content");
        BrandLine brandLine = new BrandLine();
        brandLine.setLineName("Test Brand Line");
        request.setBrandLine(brandLine);

        when(brandLineRepository.findByLineName(anyString())).thenReturn(Optional.empty());

        AppException exception = assertThrows(AppException.class, () -> newsService.createNews(request, file));
        assertEquals(ErrorCode.BRAND_LINE_NOT_EXISTED, exception.getErrorCode());
        verify(newsRepository, never()).save(any(News.class));
        verify(googleCloudStorageService, never()).uploadNewsBannerToGCS(any(MultipartFile.class), anyString());
    }

    @Test
    void getAllNews_success() {
        News news = new News();
        when(newsRepository.findAll()).thenReturn(List.of(news));
        when(newsMapper.toNewsResponse(any(News.class))).thenReturn(new NewsResponse());

        List<NewsResponse> response = newsService.getAllNews();

        assertEquals(1, response.size());
        verify(newsRepository).findAll();
    }

    @Test
    void getNewsById_success() {
        String newsId = "1";
        News news = new News();
        news.setId(newsId);
        when(newsRepository.findById(newsId)).thenReturn(Optional.of(news));
        when(newsMapper.toNewsResponse(any(News.class))).thenReturn(new NewsResponse());

        NewsResponse response = newsService.getNewsById(newsId);

        assertNotNull(response);
        verify(newsRepository).findById(newsId);
        verify(newsMapper).toNewsResponse(news);
    }

    @Test
    void getNewsByIdNotFound_fail() {
        String newsId = "1";
        when(newsRepository.findById(newsId)).thenReturn(Optional.empty());

        AppException exception = assertThrows(AppException.class, () -> newsService.getNewsById(newsId));
        assertEquals(ErrorCode.NEWS_NOT_EXISTED, exception.getErrorCode());
        verify(newsRepository).findById(newsId);
        verify(newsMapper, never()).toNewsResponse(any(News.class));
    }

    @Test
    void updateNews_Success() throws IOException {
        String newsId = "1";
        NewsRequest request = new NewsRequest();
        request.setTitle("Updated Title");
        request.setContent("Updated Content");
        BrandLine brandLine = new BrandLine();
        brandLine.setLineName("Updated Brand Line");
        request.setBrandLine(brandLine);

        News existingNews = new News();
        existingNews.setId(newsId);

        when(newsRepository.findById(newsId)).thenReturn(Optional.of(existingNews));
        when(brandLineRepository.findByLineName(anyString())).thenReturn(Optional.of(brandLine));
        when(googleCloudStorageService.uploadNewsBannerToGCS(any(MultipartFile.class), anyString()))
                .thenReturn("Updated URL");
        when(newsRepository.save(any(News.class))).thenReturn(existingNews);
        when(newsMapper.toNewsResponse(any(News.class))).thenReturn(new NewsResponse());

        NewsResponse response = newsService.updateNews(request, newsId, file);

        assertNotNull(response);
        verify(newsRepository).findById(newsId);
        verify(brandLineRepository).findByLineName(anyString());
        verify(googleCloudStorageService).uploadNewsBannerToGCS(any(MultipartFile.class), anyString());
        verify(newsRepository).save(any(News.class));
        verify(newsMapper).toNewsResponse(existingNews);
    }

    @Test
    void updateNews_NotFound_fail() {
        String newsId = "1";
        NewsRequest request = new NewsRequest();
        request.setTitle("Updated Title");
        request.setContent("Updated Content");
        BrandLine brandLine = new BrandLine();
        brandLine.setLineName("Updated Brand Line");
        request.setBrandLine(brandLine);

        when(newsRepository.findById(newsId)).thenReturn(Optional.empty());

        AppException exception = assertThrows(AppException.class, () -> newsService.updateNews(request, newsId, file));
        assertEquals(ErrorCode.NEWS_NOT_EXISTED, exception.getErrorCode());
        verify(newsRepository).findById(newsId);
        verify(brandLineRepository, never()).findByLineName(anyString());
        verify(newsRepository, never()).save(any(News.class));
        verify(newsMapper, never()).toNewsResponse(any(News.class));
    }

    @Test
    void updateNews_BrandLineNotFound_fail() {
        String newsId = "1";
        NewsRequest request = new NewsRequest();
        request.setTitle("Updated Title");
        request.setContent("Updated Content");
        BrandLine brandLine = new BrandLine();
        brandLine.setLineName("Updated Brand Line");
        request.setBrandLine(brandLine);

        News existingNews = new News();
        existingNews.setId(newsId);

        when(newsRepository.findById(newsId)).thenReturn(Optional.of(existingNews));
        when(brandLineRepository.findByLineName(anyString())).thenReturn(Optional.empty());

        AppException exception = assertThrows(AppException.class, () -> newsService.updateNews(request, newsId, file));
        assertEquals(ErrorCode.BRAND_LINE_NOT_EXISTED, exception.getErrorCode());
        verify(newsRepository).findById(newsId);
        verify(brandLineRepository).findByLineName(anyString());
        verify(newsRepository, never()).save(any(News.class));
        verify(newsMapper, never()).toNewsResponse(any(News.class));
    }

    @Test
    void deleteNews_Success() {
        String newsId = "1";

        doNothing().when(newsRepository).deleteById(newsId);

        newsService.deleteNews(newsId);

        verify(newsRepository).deleteById(newsId);
    }

    @Test
    void deleteNews_NotFound_fail() {
        String newsId = "1";

        doThrow(new AppException(ErrorCode.NEWS_NOT_EXISTED))
                .when(newsRepository)
                .deleteById(newsId);

        AppException exception = assertThrows(AppException.class, () -> newsService.deleteNews(newsId));
        assertEquals(ErrorCode.NEWS_NOT_EXISTED, exception.getErrorCode());
        verify(newsRepository).deleteById(newsId);
    }
}
