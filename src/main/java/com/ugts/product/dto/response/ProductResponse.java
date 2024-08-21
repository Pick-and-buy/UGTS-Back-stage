package com.ugts.product.dto.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ugts.brand.dto.GeneralBrandInformationDto;
import com.ugts.brandCollection.entity.BrandCollection;
import com.ugts.brandLine.dto.GeneralBrandLineInformationDto;
import com.ugts.category.entity.Category;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductResponse {
    String id;
    String name;
    String thumbnail;
    List<ProductImageResponse> images;
    GeneralBrandInformationDto brand;
    GeneralBrandLineInformationDto brandLine;
    BrandCollection brandCollection;

    @JsonIgnoreProperties("hibernateLazyInitializer")
    Category category;

    double price;
    String color;
    String size;
    String width;
    String height;
    String length;
    String referenceCode;
    String manufactureYear;
    String exteriorMaterial;
    String interiorMaterial;
    String condition;
    String accessories;
    String dateCode;
    String serialNumber;
    String purchasedPlace;
    String story;
    String verifiedLevel;
    String productVideo;
    String originalReceiptProof;
}
