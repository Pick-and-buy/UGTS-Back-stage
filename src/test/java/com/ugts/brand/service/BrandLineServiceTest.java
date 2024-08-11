//package com.ugts.brand.service;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.Mockito.*;
//import static org.springframework.test.util.AssertionErrors.assertEquals;
//
//import java.io.IOException;
//import java.util.Collections;
//import java.util.Date;
//import java.util.List;
//import java.util.Optional;
//
//import com.ugts.brand.dto.request.BrandRequest;
//import com.ugts.brand.entity.Brand;
//import com.ugts.brand.repository.BrandRepository;
//import com.ugts.brandLine.dto.request.BrandLineRequest;
//import com.ugts.brandLine.dto.response.BrandLineResponse;
//import com.ugts.brandLine.entity.BrandLine;
//import com.ugts.brandLine.mapper.BrandLineMapper;
//import com.ugts.brandLine.repository.BrandLineRepository;
//import com.ugts.brandLine.service.impl.BrandLineServiceImpl;
//import com.ugts.cloudService.GoogleCloudStorageService;
//import com.ugts.exception.AppException;
//import com.ugts.exception.ErrorCode;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.TestPropertySource;
//import org.springframework.web.multipart.MultipartFile;
//
//@SpringBootTest
//@TestPropertySource("/test.properties")
//public class BrandLineServiceTest {
//    @Mock
//    private BrandRepository brandRepository;
//
//    @Mock
//    private BrandLineRepository brandLineRepository;
//
//    @Mock
//    private GoogleCloudStorageService googleCloudStorageService;
//
//    @Mock
//    private BrandLineMapper brandLineMapper;
//
//    @InjectMocks
//    private BrandLineServiceImpl brandLineServiceImpl;
//
//    @Test
//    void createBrandLine_success() throws IOException {
//        Brand brand = new Brand();
//        BrandLineRequest brandLineRequest = new BrandLineRequest();
//        brandLineRequest.setBrandRequest(new BrandRequest("Gucci"));
//        brandLineRequest.setLineName("Deco");
//        brandLineRequest.setDescription(
//                "The Gucci Deco line captures the essence of an Italian summer with luxurious and vibrant designs.");
//        brandLineRequest.setLaunchDate(String.valueOf(new Date()));
//        brandLineRequest.setSignatureFeatures("Marmont");
//        brandLineRequest.setPriceRange("500 - 10.000");
//
//        when(brandRepository.findByName(anyString())).thenReturn(Optional.of(brand));
//        when(brandLineRepository.existsByBrandAndLineName(any(Brand.class), anyString()))
//                .thenReturn(false);
//        when(brandLineRepository.save(any(BrandLine.class))).thenReturn(new BrandLine());
//        when(googleCloudStorageService.uploadBrandLineImagesToGCS(any(MultipartFile[].class), anyLong()))
//                .thenReturn(Collections.singletonList("url"));
//        when(brandLineMapper.toBrandLineResponse(any(BrandLine.class))).thenReturn(new BrandLineResponse());
//
//        BrandLineResponse response = brandLineServiceImpl.createBrandLine(brandLineRequest, new MultipartFile[0]);
//
//        assertNotNull(response);
//        verify(brandRepository, times(1)).findByName(anyString());
//        verify(brandLineRepository, times(1)).existsByBrandAndLineName(any(Brand.class), anyString());
//    }
//
//    @Test
//    void createBrandLine_BrandDoesNotExist_fail() {
//        BrandLineRequest brandLineRequest = new BrandLineRequest();
//        brandLineRequest.setBrandRequest(new BrandRequest("Dior"));
//
//        when(brandRepository.findByName(anyString())).thenReturn(Optional.empty());
//
//        AppException exception = assertThrows(AppException.class, () -> {
//            brandLineServiceImpl.createBrandLine(brandLineRequest, new MultipartFile[0]);
//        });
//
//        assertEquals("Brand not exist!", ErrorCode.BRAND_NOT_EXISTED, exception.getErrorCode());
//        verify(brandRepository, times(1)).findByName(anyString());
//    }
//
//    @Test
//    void createBrandLine_BrandLineAlreadyExists_fail() {
//        Brand brand = new Brand();
//        BrandLineRequest brandLineRequest = new BrandLineRequest();
//        BrandRequest brandRequest = new BrandRequest();
//        brandRequest.setName("Gucci");
//        brandLineRequest.setBrandRequest(brandRequest);
//        brandLineRequest.setLineName("Deco");
//
//        when(brandRepository.findByName(anyString())).thenReturn(Optional.of(brand));
//        when(brandLineRepository.existsByBrandAndLineName(any(Brand.class), anyString()))
//                .thenReturn(true);
//
//        AppException exception = assertThrows(AppException.class, () -> {
//            brandLineServiceImpl.createBrandLine(brandLineRequest, new MultipartFile[0]);
//        });
//
//        assertEquals("Brand line has already existed!", ErrorCode.BRAND_Line_ALREADY_EXISTED, exception.getErrorCode());
//        verify(brandRepository, times(1)).findByName(anyString());
//        verify(brandLineRepository, times(1)).existsByBrandAndLineName(any(Brand.class), anyString());
//    }
//
//    @Test
//    void getBrandLines_success() {
//        BrandLine brandLine = new BrandLine();
//        when(brandLineRepository.findAll()).thenReturn(Collections.singletonList(brandLine));
//        when(brandLineMapper.toBrandLineResponse(any(BrandLine.class))).thenReturn(new BrandLineResponse());
//
//        List<BrandLineResponse> responses = brandLineServiceImpl.getBrandLines();
//
//        assertNotNull(responses);
//        verify(brandLineRepository, times(1)).findAll();
//        verify(brandLineMapper, times(1)).toBrandLineResponse(any(BrandLine.class));
//    }
//
//    @Test
//    void getBrandLineByLineName_success() {
//        BrandLine brandLine = new BrandLine();
//        when(brandLineRepository.findByLineName(anyString())).thenReturn(Optional.of(brandLine));
//        when(brandLineMapper.toBrandLineResponse(any(BrandLine.class))).thenReturn(new BrandLineResponse());
//
//        BrandLineResponse response = brandLineServiceImpl.getBrandLineByLineName("Deco");
//
//        assertNotNull(response);
//        verify(brandLineRepository, times(1)).findByLineName(anyString());
//        verify(brandLineMapper, times(1)).toBrandLineResponse(any(BrandLine.class));
//    }
//
//    @Test
//    void getBrandLineByLineName_LineNameDoesNotExist_fail() {
//        String lineName = "Summer";
//
//        when(brandLineRepository.findByLineName(anyString())).thenReturn(Optional.empty());
//        AppException exception = assertThrows(AppException.class, () -> {
//            brandLineServiceImpl.getBrandLineByLineName(lineName);
//        });
//
//        assertEquals("Brand line not exist!", ErrorCode.BRAND_LINE_NOT_EXISTED, exception.getErrorCode());
//        verify(brandLineRepository, times(1)).findByLineName(anyString());
//        verify(brandLineMapper, times(0)).toBrandLineResponse(any(BrandLine.class));
//    }
//
//    @Test
//    void deleteBrandLine_shouldDeleteBrandLine() {
//        BrandLine brandLine = new BrandLine();
//        when(brandLineRepository.findByLineName(anyString())).thenReturn(Optional.of(brandLine));
//
//        brandLineServiceImpl.deleteBrandLine("Falls");
//
//        verify(brandLineRepository, times(1)).findByLineName(anyString());
//        verify(brandLineRepository, times(1)).delete(any(BrandLine.class));
//    }
//
//    @Test
//    void deleteBrandLine_LineNameDoesNotExist_fail() {
//        String lineName = "Summer";
//        when(brandLineRepository.findByLineName(anyString())).thenReturn(Optional.empty());
//        AppException exception = assertThrows(AppException.class, () -> {
//            brandLineServiceImpl.deleteBrandLine(lineName);
//        });
//
//        assertEquals("Brand line not exist!", ErrorCode.BRAND_LINE_NOT_EXISTED, exception.getErrorCode());
//        verify(brandLineRepository, times(1)).findByLineName(anyString());
//        verify(brandLineRepository, times(0)).delete(any(BrandLine.class));
//    }
//
//    @Test
//    void getBrandLineByBrandName_success() {
//        BrandLine brandLine = new BrandLine();
//        when(brandLineRepository.findBrandLinesByBrandName(anyString()))
//                .thenReturn(Collections.singletonList(brandLine));
//        when(brandLineMapper.toBrandLineResponse(any(BrandLine.class))).thenReturn(new BrandLineResponse());
//
//        List<BrandLineResponse> responses = brandLineServiceImpl.getBrandLineByBrandName("Gucci");
//
//        assertNotNull(responses);
//        verify(brandLineRepository, times(1)).findBrandLinesByBrandName(anyString());
//        verify(brandLineMapper, times(1)).toBrandLineResponse(any(BrandLine.class));
//    }
//
//    @Test
//    void getBrandLineByBrandName_BrandNameHasNoBrandLines_fail() {
//        String brandName = "Gucci";
//
//        when(brandLineRepository.findBrandLinesByBrandName(anyString())).thenReturn(Collections.emptyList());
//
//        List<BrandLineResponse> responses = brandLineServiceImpl.getBrandLineByBrandName(brandName);
//
//        assertNotNull(responses);
//        assertTrue(responses.isEmpty());
//        verify(brandLineRepository, times(1)).findBrandLinesByBrandName(anyString());
//        verify(brandLineMapper, times(0)).toBrandLineResponse(any(BrandLine.class));
//    }
//}
