package com.ugts.brand.service.impl;

import java.util.List;

import com.ugts.brand.dto.request.CategoryRequest;
import com.ugts.brand.dto.response.CategoryResponse;
import com.ugts.brand.entity.Category;
import com.ugts.brand.mapper.CategoryMapper;
import com.ugts.brand.repository.BrandLineRepository;
import com.ugts.brand.repository.CategoryRepository;
import com.ugts.brand.service.CategoryService;
import com.ugts.exception.AppException;
import com.ugts.exception.ErrorCode;
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
    public List<CategoryResponse> getCategories() {
        return List.of();
    }

    @Override
    public CategoryResponse getCategoryByCategoryName(String categoryName) {
        return null;
    }
}