package com.ugts.rating.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ugts.user.dto.GeneralUserInformationDto;
import lombok.Data;

import java.util.Date;

@Data
public class RatingResponse {
    private String ratingId;
    private int stars;
    private String comment;
    private String ratedUserId;
    private GeneralUserInformationDto ratingUser;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Saigon")
    private Date ratedAt;
}
