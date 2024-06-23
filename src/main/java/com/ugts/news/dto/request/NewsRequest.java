package com.ugts.news.dto.request;

import com.ugts.brandLine.entity.BrandLine;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class NewsRequest {
    BrandLine brandLine;
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
