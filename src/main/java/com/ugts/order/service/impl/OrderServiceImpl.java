package com.ugts.order.service.impl;

import com.ugts.order.dto.request.CreateOrderRequest;
import com.ugts.order.dto.response.OrderResponse;
import com.ugts.order.mapper.OrderMapper;
import com.ugts.order.repository.OrderRepository;
import com.ugts.order.service.OrderService;
import com.ugts.product.repository.ProductRepository;
import com.ugts.transaction.repository.TransactionRepository;
import com.ugts.user.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class OrderServiceImpl implements OrderService {
    TransactionRepository transactionRepository;

    ProductRepository productRepository;

    OrderRepository orderRepository;

    UserRepository userRepository;

    OrderMapper orderMapper;


    @Override
    @Transactional
    @PreAuthorize("hasAnyRole('USER')")
    public OrderResponse createOrder(CreateOrderRequest orderRequest) {
        //: TODO: implement logic for ordering
        return null;
    }
}
