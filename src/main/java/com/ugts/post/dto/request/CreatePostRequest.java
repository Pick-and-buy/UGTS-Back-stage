package com.ugts.post.dto.request;

import com.ugts.brand.entity.Brand;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreatePostRequest {
    String id;
    String title;
    String description;
    Boolean isAvailable;

    Brand brand;
    String productName;
    double productPrice;
    String productColor;
    String productSize;
    String productCondition;
    String productMaterial;
    String productAccessories;
    String productDateCode;
    String productSerialNumber;
    String productPurchasedPlace;
    Boolean isVerify;
}
