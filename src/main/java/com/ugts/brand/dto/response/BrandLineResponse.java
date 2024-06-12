package com.ugts.brand.dto.response;

import java.util.Date;
import java.util.Set;

import com.ugts.brand.entity.BrandLineImage;
import com.ugts.brand.entity.Category;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BrandLineResponse {
    Long id;
    Set<Category> categories;
    String lineName;
    String description;
    String launchDate;
    String signatureFeatures;
    String priceRange;
    Boolean availableStatus;
    Date createdAt;
    Date updatedAt;
    Set<BrandLineImage> brandLineImages;
}
