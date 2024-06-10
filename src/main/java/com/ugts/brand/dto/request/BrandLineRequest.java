package com.ugts.brand.dto.request;

import com.ugts.brand.entity.BrandLineImage;
import com.ugts.brand.entity.Category;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BrandLineRequest {
    BrandRequest brandRequest;
    Category category;
    String lineName;
    String description;
    String launchDate;
    String signatureFeatures;
    String priceRange;
    Boolean availableStatus;
    BrandLineImage brandLineImage;
}
