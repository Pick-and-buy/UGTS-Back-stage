package com.ugts.order.service.impl;

import com.ugts.exception.AppException;
import com.ugts.exception.ErrorCode;
import com.ugts.order.dto.request.CreateOrderRequest;
import com.ugts.order.dto.response.OrderResponse;
import com.ugts.order.entity.OrderDetails;
import com.ugts.order.enums.OrderStatus;
import com.ugts.order.mapper.OrderMapper;
import com.ugts.order.repository.OrderRepository;
import com.ugts.order.service.OrderService;
import com.ugts.post.repository.PostRepository;
import com.ugts.product.repository.ProductRepository;
import com.ugts.transaction.repository.TransactionRepository;
import com.ugts.user.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class OrderServiceImpl implements OrderService {
    TransactionRepository transactionRepository;

    PostRepository postRepository;

    OrderRepository orderRepository;

    UserRepository userRepository;

    OrderMapper orderMapper;


    @Override
    @Transactional
    @PreAuthorize("hasAnyRole('USER')")
    public OrderResponse createOrder(CreateOrderRequest orderRequest) {
        var contextHolder = SecurityContextHolder.getContext();
        String phoneNumber = contextHolder.getAuthentication().getName();

        var buyer = userRepository
                .findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        var post = postRepository.findById(orderRequest.getPost().getId())
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_EXISTED));

        var orderDetails = OrderDetails.builder()
                .price(orderRequest.getPost().getProduct().getPrice())
                .quantity(1)
                .isFeedBack(false)
                .firstName(buyer.getFirstName())
                .lastName(buyer.getLastName())
                .email(buyer.getEmail())
                .phoneNumber(buyer.getPhoneNumber())
                .address(buyer.getAddress().toString())
                .paymentMethod(orderRequest.getPaymentMethod())
                .status(OrderStatus.PENDING)
                .build();
        return null;
    }
}
