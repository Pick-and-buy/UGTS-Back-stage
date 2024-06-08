package com.ugts.brand.mapper;

import com.ugts.brand.dto.request.BrandRequest;
import com.ugts.brand.dto.response.BrandResponse;
import com.ugts.brand.entity.Brand;
import com.ugts.brand.entity.BrandLogo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface BrandMapper {
    Brand toBrand(BrandRequest request);


    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "name", target = "name"),
            @Mapping(source = "brandLogos", target = "logoUrl", qualifiedByName = "mapBrandLogosToLogoUrl")
    })
    BrandResponse toBrandResponse(Brand brand);

    @Named("mapBrandLogosToLogoUrl")
    default String mapBrandLogosToLogoUrl(Set<BrandLogo> brandLogos) {
        if (brandLogos != null && !brandLogos.isEmpty()) {
            return brandLogos.iterator().next().getLogoUrl();
        }
        return null;
    }
}
