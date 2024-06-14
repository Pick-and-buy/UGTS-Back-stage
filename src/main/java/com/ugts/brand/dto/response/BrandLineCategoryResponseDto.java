package com.ugts.brand.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BrandLineCategoryResponseDto {
    Long id;
    String lineName;
    Boolean availableStatus;
}
