package com.ugts.post.dto.request;

import com.ugts.brand.entity.Brand;
import com.ugts.brandLine.entity.BrandLine;
import com.ugts.category.entity.Category;
import com.ugts.product.entity.Condition;
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
    Brand brand;
    BrandLine brandLine;
    Category category;
    Product product;
    Condition condition;
    Boolean boosted;
    String lastPriceForSeller;
}
