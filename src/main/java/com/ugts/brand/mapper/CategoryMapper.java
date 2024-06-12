package com.ugts.brand.mapper;

import com.ugts.brand.dto.response.CategoryResponse;
import com.ugts.brand.entity.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryResponse categoryToCategoryResponse(Category category);
}
