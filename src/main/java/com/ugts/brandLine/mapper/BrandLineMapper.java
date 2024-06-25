package com.ugts.brandLine.mapper;

import com.ugts.brandLine.dto.response.BrandLineResponse;
import com.ugts.brandLine.entity.BrandLine;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BrandLineMapper {
    BrandLineResponse toBrandLineResponse(BrandLine brandLine);
}
