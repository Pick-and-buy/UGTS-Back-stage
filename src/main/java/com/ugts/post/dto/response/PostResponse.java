package com.ugts.post.dto.response;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ugts.comment.dto.GeneralCommentInformationDto;
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

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    Date createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    Date updatedAt;

    ProductResponse product;

    Set<GeneralCommentInformationDto> comments;

    Set<GeneralUserInformationDto> likedUsers;

    Boolean boosted;

    LocalDateTime boostEndTime;

    Boolean isArchived;
}
