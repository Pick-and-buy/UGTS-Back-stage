package com.ugts.category.controller;

import java.util.List;

import com.ugts.category.dto.request.CategoryRequest;
import com.ugts.category.dto.response.CategoryResponse;
import com.ugts.category.service.CategoryService;
import com.ugts.dto.ApiResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/brand-lines/categories")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryController {

    CategoryService categoryService;

    /**
     * A description of the entire Java function.
     *
     * @param  request      A CategoryRequest object
     * @return           ApiResponse containing a new CategoryResponse objects
     */
    @PostMapping
    public ApiResponse<CategoryResponse> createCategory(@RequestBody CategoryRequest request) {
        var result = categoryService.createCategory(request);
        return ApiResponse.<CategoryResponse>builder()
                .message("Create new category success")
                .result(result)
                .build();
    }

    /**
     * Retrieves all categories and returns them in an ApiResponse.
     *
     * @return          ApiResponse containing a list of CategoryResponse objects
     */
    @GetMapping
    public ApiResponse<List<CategoryResponse>> getAllCategories() {
        var result = categoryService.getAllCategories();
        return ApiResponse.<List<CategoryResponse>>builder()
                .message("Get all categories success")
                .result(result)
                .build();
    }

    /**
     * Retrieves a category by its name and returns it in an ApiResponse.
     *
     * @param  categoryName    the name of the category to retrieve
     * @return                ApiResponse containing the retrieved CategoryResponse object
     */
    @GetMapping("/category-name")
    public ApiResponse<CategoryResponse> getCategoryByCategoryName(@RequestParam String categoryName) {
        var result = categoryService.getCategoryByCategoryName(categoryName);
        return ApiResponse.<CategoryResponse>builder()
                .message("Get category success")
                .result(result)
                .build();
    }

    /**
     * Update a category based on the provided request and category name.
     *
     * @param  request      The CategoryRequest object containing the updated category information
     * @param  categoryName The name of the category to update
     * @return              ApiResponse containing the updated CategoryResponse object
     */
    @PutMapping
    public ApiResponse<CategoryResponse> updateCategory(
            @RequestBody CategoryRequest request, @RequestParam String categoryName) {
        var result = categoryService.updateCategory(request, categoryName);
        return ApiResponse.<CategoryResponse>builder()
                .message("Update category success")
                .result(result)
                .build();
    }

    /**
     * Delete a category based on the provided category name.
     *
     * @param  categoryId    the name of the category to delete
     * @return                ApiResponse containing a message indicating the deletion success
     */
    @DeleteMapping
    public ApiResponse<String> deleteCategory(@RequestParam String categoryId) {
        categoryService.deleteCategory(categoryId);
        return ApiResponse.<String>builder().message("Delete category success").build();
    }

    @GetMapping("/line-name")
    public ApiResponse<List<CategoryResponse>> getAllCategoriesByBrandLine(@RequestParam String brandLineName) {
        var result = categoryService.getCategoriesByBrandLineName(brandLineName);
        return ApiResponse.<List<CategoryResponse>>builder()
                .message("Get all categories by brand line success")
                .result(result)
                .build();
    }
}
