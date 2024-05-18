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

import java.util.Optional;

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
}
