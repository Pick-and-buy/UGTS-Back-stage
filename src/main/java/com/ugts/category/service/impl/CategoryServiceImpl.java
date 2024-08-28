package com.ugts.category.service.impl;

import java.util.List;

import com.ugts.brandLine.repository.BrandLineRepository;
import com.ugts.category.dto.request.CategoryRequest;
import com.ugts.category.dto.response.CategoryResponse;
import com.ugts.category.entity.Category;
import com.ugts.category.mapper.CategoryMapper;
import com.ugts.category.repository.CategoryRepository;
import com.ugts.category.service.CategoryService;
import com.ugts.common.exception.AppException;
import com.ugts.common.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryServiceImpl implements CategoryService {

    CategoryRepository categoryRepository;

    BrandLineRepository brandLineRepository;

    CategoryMapper categoryMapper;

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    @Override
    public CategoryResponse createCategory(CategoryRequest request) {
        var brandLine = brandLineRepository
                .findByLineName(request.getBrandLine().getLineName())
                .orElseThrow(() -> new AppException(ErrorCode.BRAND_LINE_NOT_EXISTED));

        if (categoryRepository.existsByCategoryNameAndBrandLine(request.getCategoryName(), brandLine)) {
            throw new AppException(ErrorCode.CATEGORY_ALREADY_EXISTED);
        }
        var newCategory = Category.builder()
                .categoryName(request.getCategoryName())
                .brandLine(brandLine)
                .build();

        return categoryMapper.categoryToCategoryResponse(categoryRepository.save(newCategory));
    }

    @Override
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(categoryMapper::categoryToCategoryResponse)
                .toList();
    }

    @Override
    public CategoryResponse getCategoryByCategoryName(String categoryName) {
        var category = categoryRepository
                .findByCategoryName(categoryName)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_EXISTED));
        return categoryMapper.categoryToCategoryResponse(category);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public CategoryResponse updateCategory(CategoryRequest request, String categoryName) {
        var existingCategory = categoryRepository.findByCategoryName(request.getCategoryName());

        if (existingCategory.isPresent()
                && !existingCategory.get().getCategoryName().equals(categoryName)) {
            throw new AppException(ErrorCode.CATEGORY_ALREADY_EXISTED);
        }

        var brandLine = request.getBrandLine();
        if (brandLine == null || brandLine.getLineName() == null) {
            throw new AppException(ErrorCode.BRAND_LINE_NOT_EXISTED);
        }

        var existingBrandLine = brandLineRepository
                .findByLineName(brandLine.getLineName())
                .orElseThrow(() -> new AppException(ErrorCode.BRAND_LINE_NOT_EXISTED));

        var category = categoryRepository
                .findByCategoryNameAndBrandLineId(categoryName, existingBrandLine.getId())
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_EXISTED));

        category.setBrandLine(existingBrandLine);
        category.setCategoryName(request.getCategoryName());
        return categoryMapper.categoryToCategoryResponse(categoryRepository.save(category));
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteCategory(String categoryId) {
        var category = categoryRepository.findById(categoryId);
        if (category.isEmpty()) {
            throw new AppException(ErrorCode.CATEGORY_NOT_EXISTED);
        }
        categoryRepository.deleteById(categoryId);
    }

    @Override
    public List<CategoryResponse> getCategoriesByBrandLineName(String brandLineName) {
        var brandLine = brandLineRepository
                .findByLineName(brandLineName)
                .orElseThrow(() -> new AppException(ErrorCode.BRAND_LINE_NOT_EXISTED));

        return categoryRepository.findByLineName(brandLine.getLineName()).stream()
                .map(categoryMapper::categoryToCategoryResponse)
                .toList();
    }
}
