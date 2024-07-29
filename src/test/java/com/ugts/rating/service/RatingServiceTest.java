package com.ugts.rating.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

import java.util.Optional;

import com.ugts.comment.service.impl.CommentValidationServiceImpl;
import com.ugts.order.repository.OrderRepository;
import com.ugts.rating.RatingMapper;
import com.ugts.rating.dto.RatingRequest;
import com.ugts.rating.entity.Rating;
import com.ugts.rating.entity.StarRating;
import com.ugts.rating.repository.RatingRepository;
import com.ugts.user.entity.User;
import com.ugts.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class RatingServiceTest {
    @InjectMocks
    private IRatingService ratingService;

    @Mock
    private RatingRepository ratingRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    RatingMapper ratingMapper;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    CommentValidationServiceImpl commentValidationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testCreateRating_Success() {
        // Arrange
        RatingServiceImpl ratingService =
                new RatingServiceImpl(userRepository, ratingRepository, commentValidationService, ratingMapper, orderRepository);

        User ratingUser = new User();
        User ratedUser = new User();
        RatingRequest ratingRequest =
                new RatingRequest(StarRating.FIVE_STAR, "Great service!", "ratingUserId", "ratedUserId", "orderId");

        when(userRepository.findById(ratingUser.getId())).thenReturn(Optional.of(ratingUser));
        when(userRepository.findById(ratedUser.getId())).thenReturn(Optional.of(ratedUser));
        when(commentValidationService.filterBadWords("Great service!")).thenReturn("Great service!");

        // Act
        ratingService.createRating(ratingRequest);

        // Assert
        verify(ratingRepository, times(1)).save(any(Rating.class));
    }

    @Test
    public void test_filter_bad_words_in_comment() {
        RatingRequest ratingRequest =
                new RatingRequest(StarRating.FIVE_STAR, "BadWord comment", "ratingUserId", "ratedUserId", "orderId");
        User ratingUser = new User();
        User ratedUser = new User();
        when(userRepository.findById(ratingUser.getId())).thenReturn(Optional.of(ratingUser));
        when(userRepository.findById(ratedUser.getId())).thenReturn(Optional.of(ratedUser));
        when(commentValidationService.filterBadWords("cặc, service như đầu buồi quấn rẻ"))
                .thenReturn("Filtered comment");

        ratingService.createRating(ratingRequest);

        ArgumentCaptor<Rating> ratingCaptor = ArgumentCaptor.forClass(Rating.class);
        verify(ratingRepository).save(ratingCaptor.capture());
        assertEquals(
                "***, service như ******** quấn rẻ", ratingCaptor.getValue().getComment());
    }

    @Test
    void testCreateRating_InvalidRating() {}
}
