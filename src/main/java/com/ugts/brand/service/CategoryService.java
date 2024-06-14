package com.ugts.brand.service;

import java.util.List;

import com.ugts.brand.dto.request.CategoryRequest;
import com.ugts.brand.dto.response.CategoryResponse;

public interface CategoryService {
    CategoryResponse createCategory(CategoryRequest request);

    List<CategoryResponse> getAllCategories();

    CategoryResponse getCategoryByCategoryName(String categoryName);

    CategoryResponse updateCategory(CategoryRequest request, String categoryName);

    void deleteCategory(String categoryName);
}
