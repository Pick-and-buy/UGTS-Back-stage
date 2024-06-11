package com.ugts.brand.mapper;

import java.util.Set;

import com.ugts.brand.dto.response.BrandLineResponse;
import com.ugts.brand.entity.BrandLine;
import com.ugts.brand.entity.BrandLineImage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface BrandLineMapper {
    @Mappings({
        @Mapping(
                source = "brandLineImages",
                target = "brandLineImage.lineImageUrl",
                qualifiedByName = "mapBrandLineImagesToLogoUrl")
    })
    BrandLineResponse toBrandLineResponse(BrandLine brandLine);

    @Named("mapBrandLineImagesToLogoUrl")
    default String mapBrandLineImagesToLogoUrl(Set<BrandLineImage> brandLineImages) {
        if (brandLineImages != null && !brandLineImages.isEmpty()) {
            return brandLineImages.iterator().next().getLineImageUrl();
        }
        return null;
    }
}
