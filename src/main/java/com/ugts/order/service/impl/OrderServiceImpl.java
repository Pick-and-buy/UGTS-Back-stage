package com.ugts.order.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import com.ugts.exception.AppException;
import com.ugts.exception.ErrorCode;
import com.ugts.order.dto.request.CreateOrderRequest;
import com.ugts.order.dto.request.UpdateOrderRequest;
import com.ugts.order.dto.response.OrderResponse;
import com.ugts.order.entity.Order;
import com.ugts.order.entity.OrderDetails;
import com.ugts.order.enums.OrderStatus;
import com.ugts.order.mapper.OrderMapper;
import com.ugts.order.repository.OrderDetailsRepository;
import com.ugts.order.repository.OrderRepository;
import com.ugts.order.service.OrderService;
import com.ugts.post.repository.PostRepository;
import com.ugts.user.repository.UserRepository;
import com.ugts.user.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class OrderServiceImpl implements OrderService {
    PostRepository postRepository;

    OrderRepository orderRepository;

    UserRepository userRepository;

    OrderMapper orderMapper;

    OrderDetailsRepository orderDetailsRepository;

    UserService userService;

    /**
     * Creates a new order with the given order request.
     *
     * @param  orderRequest  the request containing the details of the order
     * @return                the response containing the details of the created order
     * @throws AppException   if the user or post is not found, or if the user is unauthorized to create the order
     */
    @Override
    @Transactional
    @PreAuthorize("hasAnyRole('USER')")
    public OrderResponse createOrder(CreateOrderRequest orderRequest) {
        var contextHolder = SecurityContextHolder.getContext();
        String phoneNumber = contextHolder.getAuthentication().getName();

        var buyer = userRepository
                .findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        var post = postRepository
                .findById(orderRequest.getPost().getId())
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_EXISTED));

        var orderDetails = OrderDetails.builder()
                .price(post.getProduct().getPrice())
                .quantity(1)
                .isFeedBack(false)
                .firstName(buyer.getFirstName())
                .lastName(buyer.getLastName())
                .email(buyer.getEmail())
                .phoneNumber(buyer.getPhoneNumber())
                // TODO: handle user address
                //                .address(buyer.getAddress().toString())
                .paymentMethod(orderRequest.getPaymentMethod())
                .status(OrderStatus.PENDING)
                .isPaid(false)
                .isRefund(false)
                .orderDate(new Date())
                .packageDate(orderRequest.getPackageDate())
                .deliveryDate(orderRequest.getDeliveryDate())
                .receivedDate(orderRequest.getReceivedDate())
                .build();

        var order = Order.builder()
                .buyer(buyer)
                .post(post)
                .orderDetails(orderDetails)
                .build();

        orderDetails.setOrder(order);

        return orderMapper.toOrderResponse(orderRepository.save(order));
    }

    /**
     * Updates the status of an order.
     *
     * @param  orderId  the ID of the order to update
     * @param  orderRequest  the request containing the updated order status
     * @return  the response containing the updated order information
     * @throws AppException  if the order, post, or user is not found, or if the user(seller) is unauthorized to update the order
     */
    @Override
    @Transactional
    @PreAuthorize("hasAnyRole('USER')")
    public OrderResponse updateOrderStatus(String orderId, UpdateOrderRequest orderRequest) {
        // Get the order
        var order = orderRepository.findById(orderId).orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

        // Get the post associated with the order
        var post = postRepository
                .findById(order.getPost().getId())
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_EXISTED));

        // Get the user who created the post
        var seller = userRepository
                .findById(post.getUser().getId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        // Get user who is updating the order
        var contextHolder = SecurityContextHolder.getContext();
        String phoneNumber = contextHolder.getAuthentication().getName();

        var user = userRepository
                .findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        // Verify that the user is authorized to update the order
        if (!Objects.equals(seller.getId(), user.getId())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        var orderDetails = order.getOrderDetails();
        orderDetails.setStatus(orderRequest.getOrderStatus());

        orderDetailsRepository.save(orderDetails);

        return orderMapper.toOrderResponse(orderRepository.save(order));
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyRole('USER')")
    public OrderResponse updateOrderDetails(String orderId, UpdateOrderRequest orderRequest) {
        // Get the order
        var order = orderRepository.findById(orderId).orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

        // Get the user who created the order
        var buyer = userRepository
                .findById(order.getBuyer().getId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        // Get user who is updating the order
        var user = userService.getProfile();

        // Verify that the user is authorized to update the order
        if (!Objects.equals(buyer.getId(), user.getId())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        order.getOrderDetails().setAddress(orderRequest.getAddress());
        order.getOrderDetails().setFirstName(orderRequest.getFirstName());
        order.getOrderDetails().setLastName(orderRequest.getLastName());
        order.getOrderDetails().setEmail(orderRequest.getEmail());
        order.getOrderDetails().setPhoneNumber(orderRequest.getPhoneNumber());
        order.getOrderDetails().setPaymentMethod(order.getOrderDetails().getPaymentMethod());

        if (orderRequest.getOrderStatus() == OrderStatus.CANCELLED){
            orderRepository.delete(order);
        } else {
            var orderDetails = order.getOrderDetails();
            orderDetails.setStatus(orderRequest.getOrderStatus());

            orderDetailsRepository.save(orderDetails);
        }

        return orderMapper.toOrderResponse(orderRepository.save(order));
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('USER')")
    public List<OrderResponse> getAllOrders() {
        var orders = orderRepository.findAll();
        return orderMapper.toOrdersResponse(orders);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('USER')")
    public OrderResponse getOrderByOrderId(@RequestParam String orderId) {
        var order = orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        return orderMapper.toOrderResponse(order);
    }
}
