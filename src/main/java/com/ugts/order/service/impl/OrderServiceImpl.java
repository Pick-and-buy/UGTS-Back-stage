package com.ugts.order.service.impl;

import com.ugts.exception.AppException;
import com.ugts.exception.ErrorCode;
import com.ugts.order.dto.request.CreateOrderRequest;
import com.ugts.order.dto.response.OrderResponse;
import com.ugts.order.entity.Order;
import com.ugts.order.entity.OrderDetails;
import com.ugts.order.enums.OrderStatus;
import com.ugts.order.mapper.OrderMapper;
import com.ugts.order.repository.OrderRepository;
import com.ugts.order.service.OrderService;
import com.ugts.product.repository.ProductRepository;
import com.ugts.transaction.entity.Transaction;
import com.ugts.transaction.enums.TransactionStatus;
import com.ugts.transaction.repository.TransactionRepository;
import com.ugts.user.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class OrderServiceImpl implements OrderService {

    ProductRepository productRepository;

    OrderRepository orderRepository;

    UserRepository userRepository;

    OrderMapper orderMapper;

    TransactionRepository transactionRepository;

    @Override
    public OrderResponse createOrder(CreateOrderRequest orderRequest) {
        var product = productRepository.findById(orderRequest.getProduct().getId())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));

        var orderDetails = OrderDetails.builder()
                .price(orderRequest.getProduct().getPrice())
                .paymentMethod(orderRequest.getOrderDetails().getPaymentMethod())
                .isRefund(false)
                .orderDate(new Date())
                .packageDate(new Date())
                .deliveryDate(new Date())
                .receivedDate(new Date())
                .build();

        // get user from context holder
        var contextHolder = SecurityContextHolder.getContext();
        String phoneNumber = contextHolder.getAuthentication().getName();

        var user = userRepository
                .findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        var order = Order.builder()
                .user(user)
                .product(product)
                .orderDetails(orderDetails)
                .status(OrderStatus.PENDING)
                .build();

        var transaction = Transaction.builder()
                .amount((int) orderDetails.getPrice())
                .currency("VND")
                .reason("Customer paid")
                .createDate(new Date())
                .status(TransactionStatus.ACTIVE)
                .user(user)
                .order(order)
                .build();

        transactionRepository.save(transaction);

        return orderMapper.toOrderResponse(orderRepository.save(order));
    }
}
