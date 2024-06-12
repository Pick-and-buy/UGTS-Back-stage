package com.ugts.brand.dto.response;

import com.ugts.brand.entity.BrandLine;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewsResponse {
    String id;
    String title;
    String content;
    BrandLine brandLine;
}
