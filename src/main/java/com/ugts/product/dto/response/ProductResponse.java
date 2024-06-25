package com.ugts.product.dto.response;

import java.util.Set;

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
    Set<ProductImageResponse> images;
    GeneralBrandInformationDto brand;
    GeneralBrandLineInformationDto brandLine;
    BrandCollection brandCollection;
    Category category;
    double price;
    String color;
    String size;
    String width;
    String height;
    String length;
    String drop;
    String fit;
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
    Boolean isVerify;
}
