package com.ugts.rating.dto;

import com.ugts.user.dto.GeneralUserInformationDto;
import lombok.Data;

@Data
public class RatingResponse {
    private String ratingId;
    private int stars;
    private String comment;
    private GeneralUserInformationDto ratingUser;
}
