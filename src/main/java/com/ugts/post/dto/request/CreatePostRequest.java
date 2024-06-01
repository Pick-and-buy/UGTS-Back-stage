package com.ugts.post.dto.request;

import com.ugts.brand.entity.Brand;
import com.ugts.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatePostRequest {
    private User user;
    private String title;
    private String description;
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
