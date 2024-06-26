package com.ugts.post.dto;

import com.ugts.product.dto.response.ProductResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GeneralPostInformationDto {
    String id;
    String title;
    String description;
    Boolean isAvailable;
    ProductResponse product;
}
