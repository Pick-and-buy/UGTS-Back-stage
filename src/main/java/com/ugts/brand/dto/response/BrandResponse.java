package com.ugts.brand.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BrandResponse {
    private String id;
    private String name;
    private String logoUrl;
}
