package com.ugts.category.mapper;

import com.ugts.category.dto.response.CategoryResponse;
import com.ugts.category.entity.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryResponse categoryToCategoryResponse(Category category);
}
