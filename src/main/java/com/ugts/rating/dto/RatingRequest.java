package com.ugts.rating.dto;

import com.ugts.rating.entity.StarRating;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RatingRequest {
    private StarRating stars;
    private String comment;
    private String ratingUserId;
    private String ratedUserId;
    private String orderId;
}
