package com.ugts.product.dto.response;

import com.ugts.brand.dto.response.BrandResponse;
import com.ugts.brand.entity.BrandCollection;
import com.ugts.brand.entity.BrandLine;
import com.ugts.brand.entity.Category;
import com.ugts.product.entity.ProductImage;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductResponse {
    String id;
    String name;
    ProductImage productImage;
    BrandResponse brand;
    BrandLine brandLine;
    BrandCollection brandCollection;
    Category category;
    double price;
    String exteriorColor;
    String interiorColor;
    String size;
    String width;
    String height;
    String length;
    String drop;
    String fit;
    String referenceCode;
    String manufactureYear;
    String material;
    String condition;
    String accessories;
    String dateCode;
    String serialNumber;
    String purchasedPlace;
    String story;

    Boolean isVerify;
}
