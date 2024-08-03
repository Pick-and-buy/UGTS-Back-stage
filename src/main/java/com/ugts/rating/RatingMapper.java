package com.ugts.rating;

import com.ugts.rating.dto.RatingResponse;
import com.ugts.rating.entity.Rating;
import org.springframework.stereotype.Component;

@Component
public class RatingMapper {

    public RatingResponse toRatingResponse(Rating rating) {
        RatingResponse ratingResponse = new RatingResponse();
        ratingResponse.setRatingId(rating.getRatingId());
        ratingResponse.setComment(rating.getComment());
        ratingResponse.setRatingUserId(rating.getRatingUser().getId());
        ratingResponse.setRatingUserAvatar(rating.getRatingUser().getAvatar());
        ratingResponse.setRatingUserName(rating.getRatingUser().getUsername());
        ratingResponse.setRatedUserId(rating.getRatedUser().getId());
        switch (rating.getStars()) {
            case ONE_STAR:
                ratingResponse.setStars(1);
                break;
            case TWO_STAR:
                ratingResponse.setStars(2);
                break;
            case THREE_STAR:
                ratingResponse.setStars(3);
                break;
            case FOUR_STAR:
                ratingResponse.setStars(4);
                break;
            case FIVE_STAR:
                ratingResponse.setStars(5);
                break;
        }
        return ratingResponse;
    }
}
