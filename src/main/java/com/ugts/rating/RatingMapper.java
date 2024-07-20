package com.ugts.rating;

import com.ugts.rating.dto.RatingResponse;
import com.ugts.rating.entity.Rating;
import org.springframework.stereotype.Component;

@Component
public class RatingMapper {

    public RatingResponse toRatingResponse(Rating rating) {
        RatingResponse ratingResponse = new RatingResponse();
        ratingResponse.setRatingId(rating.getRatingId());
        ratingResponse.setStars(rating.getStars());
        ratingResponse.setComment(rating.getComment());
        ratingResponse.setRatingUserId(rating.getRatingUser().getId());
        ratingResponse.setRatingUserAvatar(rating.getRatingUser().getAvatar());
        ratingResponse.setRatingUserName(rating.getRatingUser().getUsername());
        ratingResponse.setRatedUserId(rating.getRatedUser().getId());
        return ratingResponse;
    }

}
