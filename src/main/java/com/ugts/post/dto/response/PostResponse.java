package com.ugts.post.dto.response;

import java.util.Date;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ugts.comment.GeneralCommentInformationDto;
import com.ugts.product.dto.response.ProductResponse;
import com.ugts.user.dto.GeneralUserInformationDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostResponse {
    @JsonIgnoreProperties("hibernateLazyInitializer")
    GeneralUserInformationDto user;

    String id;
    String title;
    String description;
    Boolean isAvailable;
    Date createdAt;
    Date updatedAt;
    ProductResponse product;
    Set<GeneralCommentInformationDto> comments;
}
