package com.ugts.post.dto.request;

import com.ugts.brand.entity.Brand;
import com.ugts.brandLine.entity.BrandLine;
import com.ugts.category.entity.Category;
import com.ugts.product.entity.Condition;
import com.ugts.product.entity.Product;
import com.ugts.product.entity.VerifiedLevel;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreatePostRequest {
    String id;
    //    String title;
    String description;
    Boolean isAvailable;
    Brand brand;
    BrandLine brandLine;
    Category category;
    Product product;
    Condition condition;
    VerifiedLevel verifiedLevel;
    Boolean boosted;
    String lastPriceForSeller;
}
