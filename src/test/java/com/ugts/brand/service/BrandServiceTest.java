//package com.ugts.brand.service;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.Mockito.*;
//
//import java.io.IOException;
//import java.util.*;
//
//import com.ugts.brand.dto.request.BrandRequest;
//import com.ugts.brand.dto.response.BrandResponse;
//import com.ugts.brand.entity.Brand;
//import com.ugts.brand.entity.BrandLogo;
//import com.ugts.brand.mapper.BrandMapper;
//import com.ugts.brand.repository.BrandRepository;
//import com.ugts.brand.service.impl.BrandServiceImpl;
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
//class BrandServiceTest {
//    @Mock
//    private BrandRepository brandRepository;
//
//    @Mock
//    private BrandMapper brandMapper;
//
//    @InjectMocks
//    private BrandServiceImpl brandService;
//
//    @Mock
//    private GoogleCloudStorageService googleCloudStorageService;
//
//    //    @MockBean
//    //    private BrandResponse brandResponse;
//
//    //    @Test
//    //    void testCreateBrand() {
//    //        // given
//    //        BrandRequest request = new BrandRequest();
//    //        request.setName("Test Brand");
//    //
//    //        Brand brand = new Brand();
//    //        brand.setName("Test Brand");
//    //
//    //        when(brandRepository.findByName(request.getName())).thenReturn(Optional.empty());
//    //        when(brandMapper.toBrand(request)).thenReturn(brand);
//    //        when(brandRepository.save(brand)).thenReturn(brand);
//    //        when(brandMapper.toBrandResponse(brand)).thenReturn(new BrandResponse());
//    //
//    //        // when
//    //        BrandResponse response = brandService.createBrand(request);
//    //
//    //        // then
//    //        verify(brandRepository).findByName(request.getName());
//    //        verify(brandMapper).toBrand(request);
//    //        verify(brandRepository).save(brand);
//    //        verify(brandMapper).toBrandResponse(brand);
//    //
//    //        assertNotNull(response);
//    //    }
//    //
//    //    @Test
//    //    void testGetAllBrands() {
//    //        // given
//    //        Brand brand1 = new Brand();
//    //        brand1.setId(1L);
//    //        brand1.setName("Brand 1");
//    //
//    //        Brand brand2 = new Brand();
//    //        brand2.setId(2L);
//    //        brand2.setName("Brand 2");
//    //
//    //        List<Brand> brands = Arrays.asList(brand1, brand2);
//    //
//    //        BrandResponse brandResponse1 = new BrandResponse();
//    //        brandResponse1.setId(1L);
//    //        brandResponse1.setName("Brand 1");
//    //
//    //        BrandResponse brandResponse2 = new BrandResponse();
//    //        brandResponse2.setId(2L);
//    //        brandResponse2.setName("Brand 2");
//    //
//    //        when(brandRepository.findAll()).thenReturn(brands);
//    //        when(brandMapper.toBrandResponse(brand1)).thenReturn(brandResponse1);
//    //        when(brandMapper.toBrandResponse(brand2)).thenReturn(brandResponse2);
//    //
//    //        // when
//    //        List<BrandResponse> responses = brandService.getAllBrands();
//    //
//    //        // then
//    //        verify(brandRepository).findAll();
//    //        verify(brandMapper).toBrandResponse(brand1);
//    //        verify(brandMapper).toBrandResponse(brand2);
//    //
//    //        assertNotNull(responses);
//    //        assertEquals(2, responses.size());
//    //        assertEquals(brandResponse1.getId(), responses.get(0).getId());
//    //        assertEquals(brandResponse1.getName(), responses.get(0).getName());
//    //        assertEquals(brandResponse2.getId(), responses.get(1).getId());
//    //        assertEquals(brandResponse2.getName(), responses.get(1).getName());
//    //    }
//    //
//    //    @Test
//    //    void testGetBrandByName() {
//    //        // given
//    //        String name = "Test Brand";
//    //
//    //        Brand brand = new Brand();
//    //        brand.setId(1L);
//    //        brand.setName(name);
//    //
//    //        BrandResponse response = new BrandResponse();
//    //        response.setId(1L);
//    //        response.setName(name);
//    //
//    //        when(brandRepository.findByName(name)).thenReturn(Optional.of(brand));
//    //        when(brandMapper.toBrandResponse(brand)).thenReturn(response);
//    //
//    //        // when
//    //        BrandResponse result = brandService.getBrandByName(name);
//    //
//    //        // then
//    //        verify(brandRepository).findByName(name);
//    //        verify(brandMapper).toBrandResponse(brand);
//    //
//    //        assertNotNull(result);
//    //        assertEquals(response.getId(), result.getId());
//    //        assertEquals(response.getName(), result.getName());
//    //    }
//    //
//    //    @Test
//    //    void testDeleteBrand() {
//    //        // given
//    //        String name = "Test Brand";
//    //
//    //        Brand brand = new Brand();
//    //        brand.setId(1L);
//    //        brand.setName(name);
//    //
//    //        when(brandRepository.findByName(name)).thenReturn(Optional.of(brand));
//    //
//    //        // when
//    //        brandService.deleteBrand(name);
//    //
//    //        // then
//    //        verify(brandRepository).findByName(name);
//    //        verify(brandRepository).delete(brand);
//    //    }
//    //
//    //    @Test
//    //    void testUpdateBrand() {
//    //        // given
//    //        String name = "Test Brand";
//    //        BrandRequest request = new BrandRequest();
//    //        request.setName("Updated Brand");
//    //
//    //        Brand existingBrand = new Brand();
//    //        existingBrand.setId(1L);
//    //        existingBrand.setName(name);
//    //
//    //        Brand updatedBrand = new Brand();
//    //        updatedBrand.setId(1L);
//    //        updatedBrand.setName(request.getName());
//    //
//    //        BrandResponse response = new BrandResponse();
//    //        response.setId(1L);
//    //        response.setName(request.getName());
//    //
//    //        when(brandRepository.findByName(name)).thenReturn(Optional.of(existingBrand));
//    //        when(brandRepository.save(existingBrand)).thenReturn(updatedBrand);
//    //        when(brandMapper.toBrandResponse(updatedBrand)).thenReturn(response);
//    //
//    //        // when
//    //        BrandResponse result = brandService.updateBrand(name, request);
//    //
//    //        // then
//    //        verify(brandRepository).findByName(name);
//    //        verify(brandRepository).save(existingBrand);
//    //        verify(brandMapper).toBrandResponse(updatedBrand);
//    //
//    //        assertNotNull(result);
//    //        assertEquals(response.getId(), result.getId());
//    //        assertEquals(response.getName(), result.getName());
//    //    }
//    @Test
//    void CreateBrand_success() throws IOException {
//        BrandRequest request = new BrandRequest();
//        request.setName("Louis vuitton");
//
//        Brand brand = new Brand();
//        brand.setName("Louis vuitton");
//
//        MultipartFile[] files = {};
//
//        when(brandRepository.findByName(anyString())).thenReturn(Optional.empty());
//        when(brandMapper.toBrand(any(BrandRequest.class))).thenReturn(brand);
//        when(brandRepository.save(any(Brand.class))).thenReturn(brand);
//        when(googleCloudStorageService.uploadBrandLogosToGCS(any(MultipartFile[].class), anyString()))
//                .thenReturn(List.of("url1", "url2"));
//
//        BrandLogo brandLogo1 = BrandLogo.builder().brand(brand).logoUrl("url1").build();
//        BrandLogo brandLogo2 = BrandLogo.builder().brand(brand).logoUrl("url2").build();
//
//        brand.setBrandLogos(new HashSet<>(Set.of(brandLogo1, brandLogo2)));
//
//        BrandResponse expectedResponse = new BrandResponse();
//        when(brandMapper.toBrandResponse(any(Brand.class))).thenReturn(expectedResponse);
//
//        BrandResponse actualResponse = brandService.createBrand(request, files);
//
//        assertEquals(expectedResponse, actualResponse);
//        verify(brandRepository, times(2)).save(any(Brand.class));
//    }
//
//    @Test
//    void createBrand_BrandAlreadyExists_fail() {
//        BrandRequest request = new BrandRequest();
//        request.setName("Gucci");
//
//        when(brandRepository.findByName(anyString())).thenReturn(Optional.of(new Brand()));
//
//        AppException exception =
//                assertThrows(AppException.class, () -> brandService.createBrand(request, new MultipartFile[0]));
//
//        assertEquals(ErrorCode.BRAND_EXISTED, exception.getErrorCode());
//    }
//
//    @Test
//    void createBrand_InvalidBrandName_fail() {
//        BrandRequest request = new BrandRequest();
//        request.setName(null);
//
//        MultipartFile[] files = {};
//
//        AppException exception = assertThrows(AppException.class, () -> brandService.createBrand(request, files));
//
//        assertEquals(ErrorCode.INVALID_INPUT, exception.getErrorCode());
//    }
//
//    @Test
//    void createBrand_FileUpload_fail() throws IOException {
//        BrandRequest request = new BrandRequest();
//        request.setName("Gucci");
//
//        Brand brand = new Brand();
//        brand.setName("Gucci");
//
//        MultipartFile[] files = {};
//
//        when(brandRepository.findByName(anyString())).thenReturn(Optional.empty());
//        when(brandMapper.toBrand(any(BrandRequest.class))).thenReturn(brand);
//        when(brandRepository.save(any(Brand.class))).thenReturn(brand);
//        when(googleCloudStorageService.uploadBrandLogosToGCS(any(MultipartFile[].class), anyString()))
//                .thenThrow(IOException.class);
//
//        IOException exception = assertThrows(IOException.class, () -> brandService.createBrand(request, files));
//
//        assertNotNull(exception);
//    }
//
//    @Test
//    void getAllBrands_Success() {
//        Brand brand1 = new Brand();
//        brand1.setName("Louis vuitton");
//        Brand brand2 = new Brand();
//        brand2.setName("Gucci");
//
//        BrandResponse brandResponse1 = new BrandResponse();
//        brandResponse1.setName("Louis vuitton");
//        BrandResponse brandResponse2 = new BrandResponse();
//        brandResponse2.setName("Gucci");
//
//        when(brandRepository.findAll()).thenReturn(List.of(brand1, brand2));
//        when(brandMapper.toBrandResponse(brand1)).thenReturn(brandResponse1);
//        when(brandMapper.toBrandResponse(brand2)).thenReturn(brandResponse2);
//
//        List<BrandResponse> actualResponses = brandService.getAllBrands();
//
//        assertEquals(2, actualResponses.size());
//        assertEquals("Louis vuitton", actualResponses.get(0).getName());
//        assertEquals("Gucci", actualResponses.get(1).getName());
//
//        verify(brandRepository, times(1)).findAll();
//        verify(brandMapper, times(1)).toBrandResponse(brand1);
//        verify(brandMapper, times(1)).toBrandResponse(brand2);
//    }
//
//    @Test
//    void getAllBrands_NoBrands() {
//        when(brandRepository.findAll()).thenReturn(Collections.emptyList());
//
//        List<BrandResponse> actualResponses = brandService.getAllBrands();
//
//        assertEquals(0, actualResponses.size());
//
//        verify(brandRepository, times(1)).findAll();
//        verify(brandMapper, times(0)).toBrandResponse(any(Brand.class));
//    }
//
//    @Test
//    void getBrandByName_Success() {
//        // Mock data
//        String brandName = "Louis vuitton";
//        Brand brand = new Brand();
//        brand.setName(brandName);
//
//        BrandResponse brandResponse = new BrandResponse();
//        brandResponse.setName(brandName);
//
//        when(brandRepository.findByName(brandName)).thenReturn(Optional.of(brand));
//        when(brandMapper.toBrandResponse(brand)).thenReturn(brandResponse);
//
//        BrandResponse actualResponse = brandService.getBrandByName(brandName);
//
//        assertEquals(brandName, actualResponse.getName());
//
//        verify(brandRepository, times(1)).findByName(brandName);
//        verify(brandMapper, times(1)).toBrandResponse(brand);
//    }
//
//    @Test
//    void getBrandByName_NotFound_fail() {
//        String brandName = "Dior";
//
//        when(brandRepository.findByName(brandName)).thenReturn(Optional.empty());
//
//        AppException exception = assertThrows(AppException.class, () -> brandService.getBrandByName(brandName));
//
//        assertEquals(ErrorCode.BRAND_NOT_EXISTED, exception.getErrorCode());
//
//        verify(brandRepository, times(1)).findByName(brandName);
//        verify(brandMapper, times(0)).toBrandResponse(any(Brand.class));
//    }
//
//    @Test
//    void deleteBrand_Success() {
//        String brandName = "Gucci";
//        Brand brand = new Brand();
//        brand.setName(brandName);
//
//        when(brandRepository.findByName(brandName)).thenReturn(Optional.of(brand));
//
//        brandService.deleteBrand(brandName);
//
//        verify(brandRepository, times(1)).findByName(brandName);
//        verify(brandRepository, times(1)).delete(brand);
//    }
//
//    @Test
//    void deleteBrand_NotFound_fail() {
//        String brandName = "Dior";
//
//        when(brandRepository.findByName(brandName)).thenReturn(Optional.empty());
//
//        AppException exception = assertThrows(AppException.class, () -> brandService.deleteBrand(brandName));
//
//        assertEquals(ErrorCode.BRAND_NOT_EXISTED, exception.getErrorCode());
//
//        verify(brandRepository, times(1)).findByName(brandName);
//        verify(brandRepository, times(0)).delete(any(Brand.class));
//    }
//
//    @Test
//    void updateBrand_Success() {
//        String existingBrandName = "abc";
//        BrandRequest request = new BrandRequest();
//        request.setName("Louis vuitton");
//
//        Brand existingBrand = new Brand();
//        when(brandRepository.findByName(existingBrandName)).thenReturn(Optional.of(existingBrand));
//        when(brandRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
//
//        BrandResponse updatedBrandResponse = new BrandResponse();
//        updatedBrandResponse.setName(request.getName());
//        when(brandMapper.toBrandResponse(any())).thenReturn(updatedBrandResponse);
//
//        BrandResponse response = brandService.updateBrand(existingBrandName, request);
//        assertNotNull(response);
//        assertEquals(request.getName(), response.getName());
//    }
//
//    @Test
//    void updateBrand_BrandNotFound_fail() {
//        String brandName = "Chanel";
//        BrandRequest request = new BrandRequest();
//        request.setName("Chanel update");
//
//        when(brandRepository.findByName(brandName)).thenReturn(Optional.empty());
//
//        AppException exception = assertThrows(AppException.class, () -> brandService.updateBrand(brandName, request));
//
//        assertEquals(ErrorCode.BRAND_NOT_EXISTED, exception.getErrorCode());
//
//        verify(brandRepository, times(1)).findByName(brandName);
//        verify(brandRepository, times(0)).findByName(request.getName());
//        verify(brandRepository, times(0)).save(any(Brand.class));
//    }
//
//    @Test
//    void updateBrand_BrandNameAlreadyExists_fail() {
//        String brandName = "abc";
//        BrandRequest request = new BrandRequest();
//        request.setName("Gucci");
//
//        Brand existingBrand = new Brand();
//        existingBrand.setName(brandName);
//        Brand existingBrand2 = new Brand();
//        existingBrand2.setName(request.getName());
//
//        when(brandRepository.findByName(brandName)).thenReturn(Optional.of(existingBrand));
//        when(brandRepository.findByName(request.getName())).thenReturn(Optional.of(existingBrand2));
//
//        AppException exception = assertThrows(AppException.class, () -> brandService.updateBrand(brandName, request));
//
//        assertEquals(ErrorCode.BRAND_EXISTED, exception.getErrorCode());
//    }
//}
