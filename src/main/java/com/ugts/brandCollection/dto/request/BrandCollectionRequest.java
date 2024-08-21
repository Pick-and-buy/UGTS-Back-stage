package com.ugts.brandCollection.dto.request;

import java.util.Date;

import com.ugts.brand.dto.request.BrandRequest;
import com.ugts.brandCollection.entity.BrandCollectionImage;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BrandCollectionRequest {
    BrandRequest brandRequest;
    String collectionName;
    String season;
    String year;
    String theme;
    String releaseDate;
    String endDate;
    String designer;
    String description;
    Date createdAt;
    Date updatedAt;
    BrandCollectionImage brandCollectionImages;
}
