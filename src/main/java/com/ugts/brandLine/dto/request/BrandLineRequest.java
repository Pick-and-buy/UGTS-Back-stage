package com.ugts.brandLine.dto.request;

import com.ugts.brand.dto.request.BrandRequest;
import com.ugts.brandLine.entity.BrandLineImage;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
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
