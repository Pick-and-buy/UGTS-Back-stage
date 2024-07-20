package com.ugts.rating.controller;

import com.ugts.dto.ApiResponse;
import com.ugts.notification.dto.NotificationResponse;
import com.ugts.rating.dto.RatingRequest;
import com.ugts.rating.dto.RatingResponse;
import com.ugts.rating.entity.Rating;
import com.ugts.rating.service.IRatingService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/api/v1/rating")
@RequiredArgsConstructor
public class RatingController {
    private final IRatingService ratingService;

    @PostMapping
    public ApiResponse<Void> createRating(
            @Valid @RequestBody RatingRequest ratingRequest) {
        ratingService.createRating(ratingRequest);
        return ApiResponse.<Void>builder()
                .message("Success create rating")
                .build();
    }

    @GetMapping("/rated")
    public ApiResponse<List<RatingResponse>> getRatingsByRatedUser(@RequestParam @NotBlank String ratedUserId) {
        return ApiResponse.<List<RatingResponse>>builder()
                .result(ratingService.getRatingsByRatedUser(ratedUserId))
                .build();
    }

    @GetMapping("/rating")
    public ApiResponse<List<RatingResponse>> getRatingsByRatingUser(@RequestParam @NotBlank String ratingUserId) {
        return ApiResponse.<List<RatingResponse>>builder()
                .result(ratingService.getRatingsByRatingUser(ratingUserId))
                .build();
    }

}
