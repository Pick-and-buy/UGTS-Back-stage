package com.ugts.post.dto.request;

import com.ugts.brand.entity.Brand;
import com.ugts.product.entity.Product;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdatePostRequest {
    String id;
    String title;
    String description;

    Brand brand;
    Product product;
}
