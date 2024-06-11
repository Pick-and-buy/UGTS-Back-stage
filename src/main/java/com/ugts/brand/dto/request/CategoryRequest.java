package com.ugts.brand.dto.request;

import com.ugts.brand.entity.BrandLine;
import lombok.Builder;
import lombok.Data;

@Data
public class CategoryRequest {
    private String categoryName;
    private BrandLine brandLine;
}
