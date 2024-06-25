package com.ugts.brand.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.util.AssertionErrors.assertEquals;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.ugts.brand.dto.request.BrandRequest;
import com.ugts.brand.entity.Brand;
import com.ugts.brand.repository.BrandRepository;
import com.ugts.brandCollection.dto.request.BrandCollectionRequest;
import com.ugts.brandCollection.dto.response.BrandCollectionResponse;
import com.ugts.brandCollection.entity.BrandCollection;
import com.ugts.brandCollection.mapper.BrandCollectionMapper;
import com.ugts.brandCollection.repository.BrandCollectionRepository;
import com.ugts.brandCollection.service.impl.BrandCollectionServiceImpl;
import com.ugts.cloudService.GoogleCloudStorageService;
import com.ugts.exception.AppException;
import com.ugts.exception.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.multipart.MultipartFile;

@SpringBootTest
@TestPropertySource("/test.properties")
public class BrandCollectionServiceTest {
    @Mock
    private BrandRepository brandRepository;

    @Mock
    private BrandCollectionRepository brandCollectionRepository;

    @Mock
    private GoogleCloudStorageService googleCloudStorageService;

    @Mock
    private BrandCollectionMapper brandCollectionMapper;

    @InjectMocks
    private BrandCollectionServiceImpl brandCollectionService;

    private Brand brand;
    private BrandCollectionRequest request;
    private MultipartFile[] files;

    @BeforeEach
    void setUp() {
        brand = Brand.builder().id(1L).name("Gucci").build();

        BrandRequest brandRequest = new BrandRequest();
        brandRequest.setName("Gucci");

        request = new BrandCollectionRequest();
        request.setBrandRequest(brandRequest);
        request.setCollectionName("Chanel Marmont Summer 2024");
        request.setSeason("Summer");
        request.setYear(String.valueOf(2024));
        request.setTheme("Essence of Italian seaside, coastal allure, and carefree summer vibes");
        request.setReleaseDate("24/5/2024");
        request.setEndDate(String.valueOf(new Date()));
        request.setDesigner("Sabato De Sarno");
        request.setDescription(
                "The Gucci Lido Summer 2024 collection captures the essence of an Italian summer with luxurious and vibrant designs. Inspired by the picturesque beach clubs of Italy, the collection features sophisticated swimwear, resort wear, and iconic handbags reimagined in straw effect raffia and canvas. The campaign emphasizes a spirit of escapism, spontaneity, and the joy of sun-kissed days by the seaside.");

        files = new MultipartFile[0];
    }

    @Test
    void createBrandCollection_success() throws IOException {
        when(brandRepository.findByName(anyString())).thenReturn(Optional.of(brand));
        when(brandCollectionRepository.existsByBrandAndCollectionName(any(Brand.class), anyString()))
                .thenReturn(false);
        when(brandCollectionRepository.save(any(BrandCollection.class))).thenAnswer(i -> i.getArguments()[0]);
        when(googleCloudStorageService.uploadBrandCollectionImagesToGCS(any(MultipartFile[].class), anyLong()))
                .thenReturn(List.of("http://image.url"));
        when(brandCollectionMapper.toBrandCollectionResponse(any(BrandCollection.class)))
                .thenReturn(new BrandCollectionResponse());

        BrandCollectionResponse response = brandCollectionService.createBrandCollection(request, files);

        assertNotNull(response);
        verify(brandRepository).findByName(anyString());
        verify(brandCollectionRepository).existsByBrandAndCollectionName(any(Brand.class), anyString());
        verify(brandCollectionMapper).toBrandCollectionResponse(any(BrandCollection.class));
    }

    @Test
    void createBrandCollection_brandNotExisted_fail() throws IOException {
        when(brandRepository.findByName(anyString())).thenReturn(Optional.empty());

        AppException exception = assertThrows(AppException.class, () -> {
            brandCollectionService.createBrandCollection(request, files);
        });

        assertEquals("Brand not exist!", ErrorCode.BRAND_NOT_EXISTED, exception.getErrorCode());

        verify(brandRepository).findByName(anyString());
        verify(brandCollectionRepository, never()).existsByBrandAndCollectionName(any(Brand.class), anyString());
        verify(brandCollectionRepository, never()).save(any(BrandCollection.class));
        verify(googleCloudStorageService, never())
                .uploadBrandCollectionImagesToGCS(any(MultipartFile[].class), anyLong());
        verify(brandCollectionMapper, never()).toBrandCollectionResponse(any(BrandCollection.class));
    }

    @Test
    void createBrandCollection_brandCollectionExisted_fail() throws IOException {
        when(brandRepository.findByName(anyString())).thenReturn(Optional.of(brand));
        when(brandCollectionRepository.existsByBrandAndCollectionName(any(Brand.class), anyString()))
                .thenReturn(true);

        AppException exception = assertThrows(AppException.class, () -> {
            brandCollectionService.createBrandCollection(request, files);
        });

        assertEquals(
                "Brand collection has already existed!", ErrorCode.BRAND_COLLECTION_EXISTED, exception.getErrorCode());
        verify(brandRepository).findByName(anyString());
        verify(brandCollectionRepository).existsByBrandAndCollectionName(any(Brand.class), anyString());
        verify(brandCollectionRepository, never()).save(any(BrandCollection.class));
        verify(googleCloudStorageService, never())
                .uploadBrandCollectionImagesToGCS(any(MultipartFile[].class), anyLong());
        verify(brandCollectionMapper, never()).toBrandCollectionResponse(any(BrandCollection.class));
    }
}
