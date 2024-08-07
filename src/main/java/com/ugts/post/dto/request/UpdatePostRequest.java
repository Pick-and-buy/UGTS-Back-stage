package com.ugts.post.dto.request;

import com.ugts.product.entity.Product;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdatePostRequest {
    String id;
    String title;
    String description;
    Product product;
    Boolean boosted;
    LocalDateTime boostEndTime;
}
