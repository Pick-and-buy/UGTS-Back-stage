package com.ugts.post.dto.request;

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
    Product product;
    Condition condition;
    Boolean boosted;
    String lastPriceForSeller;
}
