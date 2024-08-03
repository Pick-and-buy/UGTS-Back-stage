package com.ugts.rating;

import com.ugts.rating.dto.RatingResponse;
import com.ugts.rating.entity.Rating;
import com.ugts.user.dto.GeneralUserInformationDto;
import org.springframework.stereotype.Component;

@Component
public class RatingMapper {

    public RatingResponse toRatingResponse(Rating rating) {
        RatingResponse ratingResponse = new RatingResponse();
        GeneralUserInformationDto ratingUser = getGeneralUserInformationDto(rating);

        ratingResponse.setRatingUser(ratingUser);
        ratingResponse.setRatingId(rating.getRatingId());
        ratingResponse.setComment(rating.getComment());
        ratingResponse.setRatingUser(ratingUser);
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

    private static GeneralUserInformationDto getGeneralUserInformationDto(Rating rating) {
        GeneralUserInformationDto ratingUser = new GeneralUserInformationDto();
        ratingUser.setId(rating.getRatingUser().getId());
        ratingUser.setAvatar(rating.getRatingUser().getAvatar());
        ratingUser.setUsername(rating.getRatingUser().getUsername());
        ratingUser.setIsVerified(rating.getRatingUser().getIsVerified());
        ratingUser.setFirstName(rating.getRatingUser().getFirstName());
        ratingUser.setLastName(rating.getRatingUser().getLastName());
        ratingUser.setPhoneNumber(rating.getRatingUser().getPhoneNumber());
        return ratingUser;
    }
}
