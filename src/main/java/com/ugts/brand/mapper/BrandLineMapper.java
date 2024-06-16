package com.ugts.brand.mapper;

import com.ugts.brand.dto.response.BrandLineResponse;
import com.ugts.brand.entity.BrandLine;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BrandLineMapper {
    BrandLineResponse toBrandLineResponse(BrandLine brandLine);
}
