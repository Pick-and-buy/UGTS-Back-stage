package com.ugts.category.service;

import java.util.List;

import com.ugts.category.dto.request.CategoryRequest;
import com.ugts.category.dto.response.CategoryResponse;

public interface CategoryService {
    CategoryResponse createCategory(CategoryRequest request);

    List<CategoryResponse> getAllCategories();

    CategoryResponse getCategoryByCategoryName(String categoryName);

    CategoryResponse updateCategory(CategoryRequest request, String categoryName);

    void deleteCategory(String categoryId);

    List<CategoryResponse> getCategoriesByBrandLineName(String brandLineName);
}
