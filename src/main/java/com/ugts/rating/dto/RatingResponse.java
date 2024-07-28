package com.ugts.rating.dto;

import com.ugts.rating.entity.StarRating;
import lombok.Data;

@Data
public class RatingResponse {
    private String ratingId;
    private int stars;
    private String comment;
    private String ratingUserId;
    private String ratingUserAvatar;
    private String ratingUserName;
    private String ratedUserId;
}
