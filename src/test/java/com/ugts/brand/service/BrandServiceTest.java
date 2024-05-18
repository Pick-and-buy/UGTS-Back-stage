package com.ugts.brand.service;

import com.ugts.brand.dto.request.BrandRequest;
import com.ugts.brand.dto.response.BrandResponse;
import com.ugts.brand.entity.Brand;
import com.ugts.brand.mapper.BrandMapper;
import com.ugts.brand.repository.BrandRepository;
import com.ugts.brand.service.impl.BrandServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestPropertySource("/test.properties")
class BrandServiceTest {
    @Mock
    private BrandRepository brandRepository;

    @Mock
    private BrandMapper brandMapper;

    @InjectMocks
    private BrandServiceImpl brandService;

    @Test
    void testCreateBrand() {
        // given
        BrandRequest request = new BrandRequest();
        request.setName("Test Brand");

        Brand brand = new Brand();
        brand.setName("Test Brand");

        when(brandRepository.findByName(request.getName())).thenReturn(Optional.empty());
        when(brandMapper.toBrand(request)).thenReturn(brand);
        when(brandRepository.save(brand)).thenReturn(brand);
        when(brandMapper.toBrandResponse(brand)).thenReturn(new BrandResponse());

        // when
        BrandResponse response = brandService.createBrand(request);

        // then
        verify(brandRepository).findByName(request.getName());
        verify(brandMapper).toBrand(request);
        verify(brandRepository).save(brand);
        verify(brandMapper).toBrandResponse(brand);

        assertNotNull(response);
    }

    @Test
    void testGetAllBrands() {
        // given
        Brand brand1 = new Brand();
        brand1.setId(1L);
        brand1.setName("Brand 1");

        Brand brand2 = new Brand();
        brand2.setId(2L);
        brand2.setName("Brand 2");

        List<Brand> brands = Arrays.asList(brand1, brand2);

        BrandResponse brandResponse1 = new BrandResponse();
        brandResponse1.setId(1L);
        brandResponse1.setName("Brand 1");

        BrandResponse brandResponse2 = new BrandResponse();
        brandResponse2.setId(2L);
        brandResponse2.setName("Brand 2");

        when(brandRepository.findAll()).thenReturn(brands);
        when(brandMapper.toBrandResponse(brand1)).thenReturn(brandResponse1);
        when(brandMapper.toBrandResponse(brand2)).thenReturn(brandResponse2);

        // when
        List<BrandResponse> responses = brandService.getAllBrands();

        // then
        verify(brandRepository).findAll();
        verify(brandMapper).toBrandResponse(brand1);
        verify(brandMapper).toBrandResponse(brand2);

        assertNotNull(responses);
        assertEquals(2, responses.size());
        assertEquals(brandResponse1.getId(), responses.get(0).getId());
        assertEquals(brandResponse1.getName(), responses.get(0).getName());
        assertEquals(brandResponse2.getId(), responses.get(1).getId());
        assertEquals(brandResponse2.getName(), responses.get(1).getName());
    }

    @Test
    void testGetBrandByName() {
        // given
        String name = "Test Brand";

        Brand brand = new Brand();
        brand.setId(1L);
        brand.setName(name);

        BrandResponse response = new BrandResponse();
        response.setId(1L);
        response.setName(name);

        when(brandRepository.findByName(name)).thenReturn(Optional.of(brand));
        when(brandMapper.toBrandResponse(brand)).thenReturn(response);

        // when
        BrandResponse result = brandService.getBrandByName(name);

        // then
        verify(brandRepository).findByName(name);
        verify(brandMapper).toBrandResponse(brand);

        assertNotNull(result);
        assertEquals(response.getId(), result.getId());
        assertEquals(response.getName(), result.getName());
    }

    @Test
    void testDeleteBrand() {
        // given
        String name = "Test Brand";

        Brand brand = new Brand();
        brand.setId(1L);
        brand.setName(name);

        when(brandRepository.findByName(name)).thenReturn(Optional.of(brand));

        // when
        brandService.deleteBrand(name);

        // then
        verify(brandRepository).findByName(name);
        verify(brandRepository).delete(brand);
    }

    @Test
    void testUpdateBrand() {
        // given
        String name = "Test Brand";
        BrandRequest request = new BrandRequest();
        request.setName("Updated Brand");

        Brand existingBrand = new Brand();
        existingBrand.setId(1L);
        existingBrand.setName(name);

        Brand updatedBrand = new Brand();
        updatedBrand.setId(1L);
        updatedBrand.setName(request.getName());

        BrandResponse response = new BrandResponse();
        response.setId(1L);
        response.setName(request.getName());

        when(brandRepository.findByName(name)).thenReturn(Optional.of(existingBrand));
        when(brandRepository.save(existingBrand)).thenReturn(updatedBrand);
        when(brandMapper.toBrandResponse(updatedBrand)).thenReturn(response);

        // when
        BrandResponse result = brandService.updateBrand(name, request);

        // then
        verify(brandRepository).findByName(name);
        verify(brandRepository).save(existingBrand);
        verify(brandMapper).toBrandResponse(updatedBrand);

        assertNotNull(result);
        assertEquals(response.getId(), result.getId());
        assertEquals(response.getName(), result.getName());
    }
}
