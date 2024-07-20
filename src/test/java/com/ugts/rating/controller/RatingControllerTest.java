package com.ugts.rating.controller;

import com.ugts.dto.ApiResponse;
import com.ugts.rating.dto.RatingRequest;
import com.ugts.rating.entity.StarRating;
import com.ugts.rating.service.IRatingService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import static org.mockito.Mockito.*;

public class RatingControllerTest {
    @Mock
    private IRatingService ratingService;
    @Mock
    StarRating starRating;

    @Test
    public void test_create_rating_with_valid_request() {
        RatingRequest ratingRequest = new RatingRequest(StarRating.FIVE_STAR, "Great service", "user123", "user456");
        RatingController ratingController = new RatingController(ratingService);

        ApiResponse<Void> response = ratingController.createRating(ratingRequest);

        Assertions.assertEquals("Success create rating", response.getMessage());
        verify(ratingService, times(1)).createRating(ratingRequest);
    }
}
