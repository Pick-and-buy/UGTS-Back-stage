package com.ugts.rating.service.impl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.ugts.comment.service.impl.CommentValidationServiceImpl;
import com.ugts.common.exception.AppException;
import com.ugts.common.exception.ErrorCode;
import com.ugts.notification.entity.NotificationEntity;
import com.ugts.notification.entity.NotificationType;
import com.ugts.notification.service.impl.NotificationServiceImpl;
import com.ugts.order.entity.Order;
import com.ugts.order.enums.OrderStatus;
import com.ugts.order.repository.OrderRepository;
import com.ugts.rating.dto.RatingRequest;
import com.ugts.rating.dto.RatingResponse;
import com.ugts.rating.entity.Rating;
import com.ugts.rating.entity.StarRating;
import com.ugts.rating.mapper.RatingMapper;
import com.ugts.rating.repository.RatingRepository;
import com.ugts.rating.service.IRatingService;
import com.ugts.user.entity.User;
import com.ugts.user.repository.UserRepository;
import com.ugts.wallet.entity.Wallet;
import com.ugts.wallet.repository.WalletRepository;
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
    private final OrderRepository orderRepository;
    private final NotificationServiceImpl notificationService;
    private final WalletRepository walletRepository;

    @Override
    @Transactional
    public void createRating(RatingRequest ratingRequest) {
        if (ratingRequest.getStars() != StarRating.ONE_STAR
                && ratingRequest.getStars() != StarRating.FIVE_STAR
                && ratingRequest.getStars() != StarRating.TWO_STAR
                && ratingRequest.getStars() != StarRating.THREE_STAR
                && ratingRequest.getStars() != StarRating.FOUR_STAR) {
            throw new AppException(ErrorCode.INVALID_STAR_RATING);
        }
        User ratingUser = userRepository
                .findById(ratingRequest.getRatingUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        User ratedUser = userRepository
                .findById(ratingRequest.getRatedUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Order order = orderRepository
                .findById(ratingRequest.getOrderId())
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        String sellerId = order.getPost().getUser().getId();
        Wallet sellerWallet = order.getPost().getUser().getWallet();
        try {
            // buyer rate seller
            if (ratedUser.getId().equals(sellerId)
                    && !order.getIsBuyerRate()
                    && ratingRequest.getRatingUserId().equals(order.getBuyer().getId())) {
                String filteredContent = commentValidationService.filterBadWords(ratingRequest.getComment());
                createRating(Rating.builder()
                        .stars(ratingRequest.getStars())
                        .comment(filteredContent)
                        .ratingUser(ratingUser)
                        .ratedUser(ratedUser)
                        .ratedAt(new Date())
                        .orderId(order.getId())
                        .build());
                order.setIsBuyerRate(true);
                // TODO: notify to seller that buyer has rate
                notificationService.createNotificationStorage(NotificationEntity.builder()
                        .delivered(false)
                        .message(ratingUser.getUsername() + " has rate you! Rate now! ")
                        .notificationType(NotificationType.RATE)
                        .userFromId(ratingUser.getId())
                        .timestamp(new Date())
                        .userToId(ratedUser.getId())
                        .userFromAvatar(ratingUser.getAvatar())
                        .orderId(order.getId())
                        .build());
            }

        } catch (Exception e) {
            log.error("An error occurred while create rating : {}", e.getMessage());
        }
        // TODO: End transaction, change transaction status to completed
        try {
            // seller rate buyer
            if (order.getIsBuyerRate()
                    && !order.getIsSellerRate()
                    && ratingRequest.getRatingUserId().equals(sellerId)) {
                String filteredContent = commentValidationService.filterBadWords(ratingRequest.getComment());
                createRating(Rating.builder()
                        .stars(ratingRequest.getStars())
                        .comment(filteredContent)
                        .ratingUser(ratingUser)
                        .ratedUser(ratedUser)
                        .ratedAt(new Date())
                        .orderId(order.getId())
                        .build());
                order.setIsSellerRate(true);
                if (order.getIsBuyerRate() && order.getIsSellerRate()) {
                    order.getOrderDetails().setStatus(OrderStatus.COMPLETED);
                    // TODO: move last price for seller into seller's balance
                    try {
                        sellerWallet.setBalance(sellerWallet.getBalance()
                                + order.getOrderDetails().getLastPriceForSeller());
                        walletRepository.save(sellerWallet);
                    } catch (Exception e) {
                        log.error("An error occurred while update seller's wallet balance : {}", e.getMessage());
                    }
                }
            }
        } catch (Exception e) {
            log.error("An error occurred while update order status : {}", e.getMessage());
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
