package com.ugts.brand.dto.response;

import java.util.Set;

import com.ugts.brand.entity.BrandLogo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BrandResponse {
    private String id;
    private String name;
    Set<BrandLogo> brandLogos;
}
