package com.ugts.product.dto.response;

import com.ugts.brand.entity.Brand;
import com.ugts.brand.entity.BrandCollection;
import com.ugts.brand.entity.BrandLine;
import com.ugts.brand.entity.Category;
import com.ugts.product.entity.ProductImage;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductResponse {
    String id;
    String name;
    ProductImage productImage;
    Brand brand;
    BrandLine brandLine;
    BrandCollection brandCollection;
    Category category;
    double price;
    String color;
    String size;
    String condition;
    String material;
    String accessories;
    String dateCode;
    String serialNumber;
    String purchasedPlace;
    String story;
    Boolean isVerify;
}
