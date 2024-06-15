package com.ugts.post.dto.request;

import com.ugts.brand.entity.Brand;
import com.ugts.brand.entity.BrandLine;
import com.ugts.brand.entity.Category;
import com.ugts.product.entity.Condition;
import com.ugts.product.entity.Product;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreatePostRequest {
    String id;
    String title;
    String description;
    Boolean isAvailable;
    Brand brand;
    BrandLine brandLine;
    Category category;
    Product product;
    Condition condition;
}
