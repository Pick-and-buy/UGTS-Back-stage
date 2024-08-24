package com.ugts.post.dto.request;

import com.ugts.brand.entity.Brand;
import com.ugts.brandLine.entity.BrandLine;
import com.ugts.category.entity.Category;
import com.ugts.product.entity.Product;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdatePostRequest {
    String id;
    String description;
    Product product;
    Brand brand;
    Category category;
    BrandLine brandLine;
    Boolean boosted;
    String lastPriceForSeller;
}
