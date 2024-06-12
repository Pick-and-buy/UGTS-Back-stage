package com.ugts.brand.dto.request;

import com.ugts.brand.entity.BrandLine;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class NewsRequest {
    BrandLine brandLine;
    String title;
    String content;
}
