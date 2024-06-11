package com.ugts.brand.dto.request;

import com.ugts.brand.entity.BrandLineImage;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BrandLineRequest {
    BrandRequest brandRequest;
    String lineName;
    String description;
    String launchDate;
    String signatureFeatures;
    String priceRange;
    Boolean availableStatus;
    BrandLineImage brandLineImage;
}
