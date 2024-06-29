package com.ugts.category.dto.response;

import com.ugts.brand.dto.response.BrandLineCategoryResponseDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryResponse {
    String id;
    String categoryName;
    BrandLineCategoryResponseDto brandLine;
}
