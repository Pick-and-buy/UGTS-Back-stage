package com.ugts.post.dto.request;

import com.ugts.brand.entity.Brand;
<<<<<<< Updated upstream
=======
import com.ugts.brand.entity.BrandLine;
import com.ugts.brand.entity.Category;
import com.ugts.product.entity.Condition;
>>>>>>> Stashed changes
import com.ugts.product.entity.Product;
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
    Product product;
    Condition condition;
}
