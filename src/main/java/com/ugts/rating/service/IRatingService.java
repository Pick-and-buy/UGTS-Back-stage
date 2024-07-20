package com.ugts.rating.service;

import com.ugts.rating.dto.RatingRequest;
import com.ugts.rating.dto.RatingResponse;
import com.ugts.rating.entity.Rating;

import java.util.List;

public interface IRatingService {
    void createRating(RatingRequest ratingRequest);
    List<RatingResponse> getRatingsByRatingUser(String ratedUserId);
    List<RatingResponse> getRatingsByRatedUser(String ratingUserId);
}
