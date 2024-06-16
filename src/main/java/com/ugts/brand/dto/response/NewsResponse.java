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
    BrandLine brandLine;
    String id;
    String banner;
    String title;
    String content;
    String subTitle1;
    String subContent1;
    String subTitle2;
    String subContent2;
    String subTitle3;
    String subContent3;
}
