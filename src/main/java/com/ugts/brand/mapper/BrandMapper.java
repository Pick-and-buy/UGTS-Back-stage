package com.ugts.brand.mapper;

import com.ugts.brand.dto.request.CreateBrandRequest;
import com.ugts.brand.dto.response.BrandResponse;
import com.ugts.brand.entity.Brand;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BrandMapper {
    Brand toBrand(CreateBrandRequest request);

    BrandResponse toBrandResponse(Brand brand);
}
