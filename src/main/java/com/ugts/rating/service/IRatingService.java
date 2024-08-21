package com.ugts.rating.service;

import java.util.List;

import com.ugts.rating.dto.RatingRequest;
import com.ugts.rating.dto.RatingResponse;

public interface IRatingService {
    void createRating(RatingRequest ratingRequest);

    List<RatingResponse> getRatingsByRatingUser(String ratedUserId);

    List<RatingResponse> getRatingsByRatedUser(String ratingUserId);
}
