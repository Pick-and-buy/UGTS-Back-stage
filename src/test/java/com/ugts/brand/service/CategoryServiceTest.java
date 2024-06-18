package com.ugts.brand.service;

import com.ugts.brand.dto.request.CategoryRequest;
import com.ugts.brand.dto.response.CategoryResponse;
import com.ugts.brand.entity.BrandLine;
import com.ugts.brand.entity.Category;
import com.ugts.brand.mapper.CategoryMapper;
import com.ugts.brand.repository.BrandLineRepository;
import com.ugts.brand.repository.CategoryRepository;
import com.ugts.brand.service.impl.CategoryServiceImpl;
import com.ugts.exception.AppException;
import com.ugts.exception.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
@TestPropertySource("/test.properties")
public class CategoryServiceTest {
    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private BrandLineRepository brandLineRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private CategoryRequest categoryRequest;
    private BrandLine brandLine;
    private Category category;

    private CategoryResponse categoryResponse;

    @BeforeEach
    void setUp() {
        brandLine = BrandLine.builder().lineName("Marmont").build();
        categoryRequest = new CategoryRequest();
        categoryRequest.setCategoryName("Mini Bags");
        categoryRequest.setBrandLine(brandLine);
        category = Category.builder().categoryName("Mini Bags").brandLine(brandLine).build();
        categoryResponse = new CategoryResponse();
        categoryResponse.setCategoryName("Mini Bags");
    }
    @Test
    void createCategory_success() {
        when(brandLineRepository.findByLineName(anyString())).thenReturn(Optional.of(brandLine));
        when(categoryRepository.existsByCategoryNameAndBrandLine(anyString(), any(BrandLine.class))).thenReturn(false);
        when(categoryRepository.save(any(Category.class))).thenReturn(category);
        when(categoryMapper.categoryToCategoryResponse(any(Category.class))).thenReturn(categoryResponse);

        CategoryResponse response = categoryService.createCategory(categoryRequest);

        assertNotNull(response);
        assertEquals("Mini Bags", response.getCategoryName());
        verify(brandLineRepository).findByLineName("Marmont");
        verify(categoryRepository).existsByCategoryNameAndBrandLine("Mini Bags", brandLine);
        verify(categoryRepository).save(category);
        verify(categoryMapper).categoryToCategoryResponse(category);
    }

    @Test
    void createCategory_WhenBrandLineNotFound_fail() {
        when(brandLineRepository.findByLineName(anyString())).thenReturn(Optional.empty());

        AppException exception = assertThrows(AppException.class, () -> categoryService.createCategory(categoryRequest));

        assertEquals(ErrorCode.BRAND_LINE_NOT_EXISTED, exception.getErrorCode());
        verify(brandLineRepository).findByLineName("Marmont");
        verify(categoryRepository, never()).existsByCategoryNameAndBrandLine(anyString(), any(BrandLine.class));
        verify(categoryRepository, never()).save(any(Category.class));
        verify(categoryMapper, never()).categoryToCategoryResponse(any(Category.class));
    }

    @Test
    void createCategory_WhenCategoryAlreadyExists_fail() {
        when(brandLineRepository.findByLineName(anyString())).thenReturn(Optional.of(brandLine));
        when(categoryRepository.existsByCategoryNameAndBrandLine(anyString(), any(BrandLine.class))).thenReturn(true);

        AppException exception = assertThrows(AppException.class, () -> categoryService.createCategory(categoryRequest));

        assertEquals(ErrorCode.CATEGORY_ALREADY_EXISTED, exception.getErrorCode());
        verify(brandLineRepository).findByLineName("Marmont");
        verify(categoryRepository).existsByCategoryNameAndBrandLine("Mini Bags", brandLine);
        verify(categoryRepository, never()).save(any(Category.class));
        verify(categoryMapper, never()).categoryToCategoryResponse(any(Category.class));
    }



    @Test
    void getCategoryByCategoryName_ShouldReturnCategory_WhenCategoryExists() {
        when(categoryRepository.findByCategoryName(anyString())).thenReturn(Optional.of(category));
        when(categoryMapper.categoryToCategoryResponse(any(Category.class))).thenReturn(categoryResponse);

        CategoryResponse response = categoryService.getCategoryByCategoryName("Mini Bags");

        assertNotNull(response);
        assertEquals("Mini Bags", response.getCategoryName());
        verify(categoryRepository).findByCategoryName("Mini Bags");
        verify(categoryMapper).categoryToCategoryResponse(category);
    }

    @Test
    void getCategoryByCategoryName_CategoryNotFound_fail() {
        when(categoryRepository.findByCategoryName(anyString())).thenReturn(Optional.empty());

        AppException exception = assertThrows(AppException.class, () -> categoryService.getCategoryByCategoryName("Mini Bags"));

     //   assertEquals(ErrorCode.CATEGORY_NOT_FOUND, exception.getErrorCode());
        verify(categoryRepository).findByCategoryName("Mini Bags");
        verify(categoryMapper, never()).categoryToCategoryResponse(any(Category.class));
    }
}
