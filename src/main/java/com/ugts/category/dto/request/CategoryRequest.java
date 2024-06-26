package com.ugts.category.dto.request;

import com.ugts.brandLine.entity.BrandLine;
import lombok.Data;

@Data
public class CategoryRequest {
    private String categoryName;
    private BrandLine brandLine;
}
