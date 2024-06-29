package com.ugts.brand.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import com.ugts.brandLine.entity.BrandLine;
import com.ugts.brandLine.repository.BrandLineRepository;
import com.ugts.category.dto.request.CategoryRequest;
import com.ugts.category.dto.response.CategoryResponse;
import com.ugts.category.entity.Category;
import com.ugts.category.mapper.CategoryMapper;
import com.ugts.category.repository.CategoryRepository;
import com.ugts.category.service.impl.CategoryServiceImpl;
import com.ugts.exception.AppException;
import com.ugts.exception.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

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
        category = Category.builder()
                .categoryName("Mini Bags")
                .brandLine(brandLine)
                .build();
        categoryResponse = new CategoryResponse();
        categoryResponse.setCategoryName("Mini Bags");
    }

    @Test
    void createCategory_success() {
        when(brandLineRepository.findByLineName(anyString())).thenReturn(Optional.of(brandLine));
        when(categoryRepository.existsByCategoryNameAndBrandLine(anyString(), any(BrandLine.class)))
                .thenReturn(false);
        when(categoryRepository.save(any(Category.class))).thenReturn(category);
        when(categoryMapper.categoryToCategoryResponse(any(Category.class))).thenReturn(categoryResponse);

        CategoryResponse response = categoryService.createCategory(categoryRequest);

        assertNotNull(response);
        assertEquals("Mini Bags", response.getCategoryName());
        verify(brandLineRepository).findByLineName("Marmont");
        verify(categoryRepository).existsByCategoryNameAndBrandLine("Mini Bags", brandLine);
        verify(categoryMapper).categoryToCategoryResponse(category);
    }

    @Test
    void createCategory_WhenBrandLineNotFound_fail() {
        when(brandLineRepository.findByLineName(anyString())).thenReturn(Optional.empty());

        AppException exception =
                assertThrows(AppException.class, () -> categoryService.createCategory(categoryRequest));

        assertEquals(ErrorCode.BRAND_LINE_NOT_EXISTED, exception.getErrorCode());
        verify(brandLineRepository).findByLineName("Marmont");
        verify(categoryRepository, never()).existsByCategoryNameAndBrandLine(anyString(), any(BrandLine.class));
        verify(categoryRepository, never()).save(any(Category.class));
        verify(categoryMapper, never()).categoryToCategoryResponse(any(Category.class));
    }

    @Test
    void createCategory_WhenCategoryAlreadyExists_fail() {
        when(brandLineRepository.findByLineName(anyString())).thenReturn(Optional.of(brandLine));
        when(categoryRepository.existsByCategoryNameAndBrandLine(anyString(), any(BrandLine.class)))
                .thenReturn(true);

        AppException exception =
                assertThrows(AppException.class, () -> categoryService.createCategory(categoryRequest));

        assertEquals(ErrorCode.CATEGORY_ALREADY_EXISTED, exception.getErrorCode());
        verify(brandLineRepository).findByLineName("Marmont");
        verify(categoryRepository).existsByCategoryNameAndBrandLine("Mini Bags", brandLine);
        verify(categoryRepository, never()).save(any(Category.class));
        verify(categoryMapper, never()).categoryToCategoryResponse(any(Category.class));
    }

    @Test
    void getCategoryByCategoryName_success() {
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

        AppException exception =
                assertThrows(AppException.class, () -> categoryService.getCategoryByCategoryName("Mini Bags"));

        assertEquals(ErrorCode.CATEGORY_NOT_EXISTED, exception.getErrorCode());
        verify(categoryRepository).findByCategoryName("Mini Bags");
        verify(categoryMapper, never()).categoryToCategoryResponse(any(Category.class));
    }

    @Test
    void updateCategory_success() {
        String categoryName = "Mini Bags";
        CategoryRequest request = new CategoryRequest();
        request.setCategoryName("Deco");

        BrandLine brandLine = new BrandLine();
        brandLine.setLineName("Marmont");
        request.setBrandLine(brandLine);

        Category existingCategory = new Category();
        existingCategory.setCategoryName(categoryName);

        BrandLine existingBrandLine = new BrandLine();
        existingBrandLine.setLineName("Marmont");

        when(categoryRepository.findByCategoryName(categoryName)).thenReturn(Optional.of(existingCategory));
        when(brandLineRepository.findByLineName("Marmont")).thenReturn(Optional.of(existingBrandLine));

        Category updatedCategory = new Category();
        updatedCategory.setCategoryName(request.getCategoryName());
        updatedCategory.setBrandLine(existingBrandLine);

        when(categoryRepository.save(any())).thenReturn(updatedCategory);

        when(categoryMapper.categoryToCategoryResponse(updatedCategory)).thenReturn(new CategoryResponse());

        CategoryResponse response = categoryService.updateCategory(request, categoryName);

        assertNotNull(response);
        assertEquals(existingCategory.getCategoryName(), updatedCategory.getCategoryName());
        verify(categoryRepository, times(1)).findByCategoryName(categoryName);
        verify(brandLineRepository, times(1))
                .findByLineName(request.getBrandLine().getLineName());
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void updateCategory_CategoryAlreadyExistsForAnotherBrandLine_fail() {
        String categoryName = "Mini Bags";
        CategoryRequest request = new CategoryRequest();
        request.setCategoryName("tote Bags");
        BrandLine brandLine = new BrandLine();
        brandLine.setLineName("Marmont");
        Category existingCategory = new Category();
        existingCategory.setCategoryName("Tote Bags");
        existingCategory.setBrandLine(new BrandLine());

        when(categoryRepository.findByCategoryName(anyString())).thenReturn(Optional.of(existingCategory));
        when(brandLineRepository.findByLineName(anyString())).thenReturn(Optional.of(brandLine));

        AppException exception = assertThrows(AppException.class, () -> {
            categoryService.updateCategory(request, categoryName);
        });

        assertEquals(ErrorCode.CATEGORY_ALREADY_EXISTED, exception.getErrorCode());
        verify(categoryRepository, times(1)).findByCategoryName(anyString());
    }

    @Test
    void updateCategory_BrandLineNotFound_fail() {
        String categoryName = "Mini Bags";
        CategoryRequest request = new CategoryRequest();
        request.setCategoryName("Deco");

        BrandLine brandLine = new BrandLine();
        brandLine.setLineName("Falls");

        request.setBrandLine(brandLine);

        Category existingCategory = new Category();
        existingCategory.setCategoryName(categoryName);

        when(categoryRepository.findByCategoryName(categoryName)).thenReturn(Optional.of(existingCategory));
        when(brandLineRepository.findByLineName("Falls")).thenReturn(Optional.empty());

        AppException exception =
                assertThrows(AppException.class, () -> categoryService.updateCategory(request, categoryName));
        assertEquals(ErrorCode.BRAND_LINE_NOT_EXISTED, exception.getErrorCode());
    }

    @Test
    void deleteCategory_success() {
        String categoryName = "Mini Bags";

        categoryService.deleteCategory(categoryName);

        verify(categoryRepository, times(1)).deleteByCategoryName(anyString());
    }

    @Test
    void getCategoriesByBrandLineName_success() {
        // Arrange
        String brandLineName = "Marmont";
        BrandLine brandLine = new BrandLine();
        brandLine.setLineName(brandLineName);

        when(brandLineRepository.findByLineName(anyString())).thenReturn(Optional.of(brandLine));
        when(categoryRepository.findByLineName(anyString())).thenReturn(List.of(new Category()));
        when(categoryMapper.categoryToCategoryResponse(any(Category.class))).thenReturn(new CategoryResponse());

        List<CategoryResponse> responses = categoryService.getCategoriesByBrandLineName(brandLineName);

        assertNotNull(responses);
        assertFalse(responses.isEmpty());
        verify(brandLineRepository, times(1)).findByLineName(anyString());
        verify(categoryRepository, times(1)).findByLineName(anyString());
        verify(categoryMapper, atLeastOnce()).categoryToCategoryResponse(any(Category.class));
    }

    @Test
    public void getCategoriesByBrandLineName_BrandLineNotExists_fail() {
        String brandLineName = "Deco";

        when(brandLineRepository.findByLineName(brandLineName)).thenReturn(Optional.empty());

        AppException exception =
                assertThrows(AppException.class, () -> categoryService.getCategoriesByBrandLineName(brandLineName));
        assertEquals(ErrorCode.BRAND_LINE_NOT_EXISTED, exception.getErrorCode());
    }
}
