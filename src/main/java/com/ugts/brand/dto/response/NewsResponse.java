package com.ugts.brand.dto.response;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
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

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    Date createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    Date updatedAt;

    String subTitle1;
    String subContent1;
    String subContent2;
    String subContent3;
    String subContent4;
    String subTitle2;
    String subContent5;
    String subContent6;
    String subContent7;
    String subTitle3;
    String subContent8;
    String subContent9;
    String subContent10;
}
