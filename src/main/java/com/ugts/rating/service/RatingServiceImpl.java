package com.ugts.rating.service;

import java.util.List;
import java.util.stream.Collectors;

import com.ugts.comment.service.impl.CommentValidationServiceImpl;
import com.ugts.exception.AppException;
import com.ugts.exception.ErrorCode;
import com.ugts.rating.RatingMapper;
import com.ugts.rating.dto.RatingRequest;
import com.ugts.rating.dto.RatingResponse;
import com.ugts.rating.entity.Rating;
import com.ugts.rating.entity.StarRating;
import com.ugts.rating.repository.RatingRepository;
import com.ugts.user.entity.User;
import com.ugts.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RatingServiceImpl implements IRatingService {
    private final UserRepository userRepository;
    private final RatingRepository ratingRepository;
    private final CommentValidationServiceImpl commentValidationService;
    private final RatingMapper ratingMapper;

    @Override
    @Transactional
    public void createRating(RatingRequest ratingRequest) {
        User ratingUser = userRepository
                .findById(ratingRequest.getRatingUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        User ratedUser = userRepository
                .findById(ratingRequest.getRatedUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        if (ratingRequest.getStars() != StarRating.ONE_STAR && ratingRequest.getStars() != StarRating.FIVE_STAR) {
            throw new AppException(ErrorCode.INVALID_STAR_RATING);
        }
        try {
            String filteredContent = commentValidationService.filterBadWords(ratingRequest.getComment());
            createRating(Rating.builder()
                    .stars(ratingRequest.getStars())
                    .comment(filteredContent)
                    .ratingUser(ratingUser)
                    .ratedUser(ratedUser)
                    .build());
            // TODO: End transaction, change transaction status to completed
        } catch (Exception e) {
            log.error("An error occurred while create rating : {}", e.getMessage());
        }
    }

    public void createRating(Rating rating) {
        ratingRepository.save(rating);
    }

    @Override
    public List<RatingResponse> getRatingsByRatingUser(String ratingUserId) {
        User ratingUser =
                userRepository.findById(ratingUserId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        return ratingRepository.findByRatingUser(ratingUser).stream()
                .map(ratingMapper::toRatingResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<RatingResponse> getRatingsByRatedUser(String ratedUserId) {
        User ratedUser =
                userRepository.findById(ratedUserId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        return ratingRepository.findByRatedUser(ratedUser).stream()
                .map(ratingMapper::toRatingResponse)
                .collect(Collectors.toList());
    }
}
