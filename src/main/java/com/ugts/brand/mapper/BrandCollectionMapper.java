package com.ugts.brand.mapper;

import com.ugts.brand.dto.response.BrandCollectionResponse;
import com.ugts.brand.entity.BrandCollection;
import com.ugts.brand.entity.BrandCollectionImage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface BrandCollectionMapper {

    @Mappings({
            @Mapping(source = "id", target = "brandCollectionImages.id"),
            @Mapping(source = "brandCollectionImages",
                    target = "brandCollectionImages.collectionImageUrl",
                    qualifiedByName = "mapBrandCollectionImagesToLogoUrl")
    })
    BrandCollectionResponse toBrandCollectionResponse(BrandCollection brandCollection);

    @Named("mapBrandCollectionImagesToLogoUrl")
    default String mapBrandCollectionImagesToLogoUrl(Set<BrandCollectionImage> brandCollectionImages) {
        if (brandCollectionImages != null && !brandCollectionImages.isEmpty()) {
            return brandCollectionImages.iterator().next().getCollectionImageUrl();
        }
        return null;
    }
}
