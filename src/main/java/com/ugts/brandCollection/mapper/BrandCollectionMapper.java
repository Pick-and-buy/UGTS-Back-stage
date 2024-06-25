package com.ugts.brandCollection.mapper;

import java.util.Set;

import com.ugts.brandCollection.dto.response.BrandCollectionResponse;
import com.ugts.brandCollection.entity.BrandCollection;
import com.ugts.brandCollection.entity.BrandCollectionImage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface BrandCollectionMapper {

    @Mappings({
        @Mapping(
                source = "brandCollectionImages",
                target = "brandCollectionImages.collectionImageUrl",
                qualifiedByName = "mapBrandCollectionImagesToImageUrl")
    })
    BrandCollectionResponse toBrandCollectionResponse(BrandCollection brandCollection);

    @Named("mapBrandCollectionImagesToImageUrl")
    default String mapBrandCollectionImagesToImageUrl(Set<BrandCollectionImage> brandCollectionImages) {
        if (brandCollectionImages != null && !brandCollectionImages.isEmpty()) {
            return brandCollectionImages.iterator().next().getCollectionImageUrl();
        }
        return null;
    }
}
