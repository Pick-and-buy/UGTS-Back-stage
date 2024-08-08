package com.ugts.order.service.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import com.ugts.exception.AppException;
import com.ugts.exception.ErrorCode;
import com.ugts.notification.entity.NotificationEntity;
import com.ugts.notification.entity.NotificationType;
import com.ugts.notification.service.NotificationServiceImpl;
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
import com.ugts.rating.dto.RatingRequest;
import com.ugts.rating.entity.StarRating;
import com.ugts.rating.service.IRatingService;
import com.ugts.user.entity.Address;
import com.ugts.user.repository.AddressRepository;
import com.ugts.user.repository.UserRepository;
import com.ugts.user.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Slf4j
public class OrderServiceImpl implements OrderService {
    PostRepository postRepository;

    OrderRepository orderRepository;

    UserRepository userRepository;

    AddressRepository addressRepository;

    OrderMapper orderMapper;

    OrderDetailsRepository orderDetailsRepository;

    UserService userService;

    IRatingService ratingService;

    private final NotificationServiceImpl notificationService;

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

        post.setIsAvailable(false);

        var address = addressRepository
                .findById(orderRequest.getAddress().getId())
                .orElseThrow(() -> new AppException(ErrorCode.ADDRESS_NOT_EXISTED));

        var orderDetails = OrderDetails.builder()
                .price(post.getProduct().getPrice())
                .quantity(1)
                .isFeedBack(false)
                .firstName(buyer.getFirstName())
                .lastName(buyer.getLastName())
                .email(buyer.getEmail())
                .phoneNumber(buyer.getPhoneNumber())
                .address(address)
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
                .isBuyerRate(false)
                .isSellerRate(false)
                .orderDetails(orderDetails)
                .build();

        orderDetails.setOrder(order);
        orderRepository.save(order);

        notificationService.createNotificationStorage(NotificationEntity.builder()
                .delivered(false)
                .message(buyer.getUsername() + " đã mua mặt hàng túi xách của bạn　😍! Kiểm tra ngay! ")
                .notificationType(NotificationType.BUY)
                .userFromId(post.getUser().getId())
                .timestamp(new Date())
                .userToId(post.getUser().getId())
                .userFromAvatar(buyer.getAvatar())
                .orderId(order.getId())
                .build());

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

        if (orderRequest.getOrderStatus() == OrderStatus.CANCELLED) {
            post.setIsAvailable(true);
            postRepository.save(post);
        }

        orderDetailsRepository.save(orderDetails);

        return orderMapper.toOrderResponse(orderRepository.save(order));
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyRole('USER')")
    public OrderResponse updateOrderDetails(String orderId, UpdateOrderRequest updateOrderRequest) {
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

        Address address = new Address();
        address.setStreet(updateOrderRequest.getAddress().getStreet());
        address.setDistrict(updateOrderRequest.getAddress().getDistrict());
        address.setProvince(updateOrderRequest.getAddress().getProvince());
        address.setCountry(updateOrderRequest.getAddress().getCountry());
        address.setAddressLine(updateOrderRequest.getAddress().getAddressLine());

        addressRepository.save(address);

        order.getOrderDetails().setFirstName(updateOrderRequest.getFirstName());
        order.getOrderDetails().setLastName(updateOrderRequest.getLastName());
        order.getOrderDetails().setEmail(updateOrderRequest.getEmail());
        order.getOrderDetails().setPhoneNumber(updateOrderRequest.getPhoneNumber());
        order.getOrderDetails().setAddress(address);
        order.getOrderDetails().setPaymentMethod(order.getOrderDetails().getPaymentMethod());

        if (updateOrderRequest.getOrderStatus() == OrderStatus.CANCELLED) {
            var orderDetails = order.getOrderDetails();
            orderDetails.setStatus(updateOrderRequest.getOrderStatus());
            orderDetailsRepository.save(orderDetails);

            var post = order.getPost();
            post.setIsAvailable(true);
            postRepository.save(post);
        }

        return orderMapper.toOrderResponse(orderRepository.save(order));
    }

    /**
     * Retrieves all orders from the order repository and maps them to a list of OrderResponse objects.
     *
     * @return         	A list of OrderResponse objects representing all orders.
     */
    @Override
    @Transactional
    @PreAuthorize("hasRole('USER')")
    public List<OrderResponse> getAllOrders() {
        var orders = orderRepository.findAll();
        return orderMapper.toOrdersResponse(orders);
    }

    /**
     * Retrieves an order by its order ID.
     *
     * @param  orderId  the ID of the order to retrieve
     * @return          the response containing the order details
     * @throws AppException  if the order is not found
     */
    @Override
    @Transactional
    @PreAuthorize("hasRole('USER')")
    public OrderResponse getOrderByOrderId(@RequestParam String orderId) {
        var order = orderRepository.findById(orderId).orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        return orderMapper.toOrderResponse(order);
    }

    @Override
    @PreAuthorize("hasRole('USER')")
    public List<OrderResponse> getOrderByOrderStatus(OrderStatus orderStatus) {
        // Retrieve orders with the specified order status from the order details repository
        List<Order> ordersByStatus = orderRepository.findOrderDetailsByOrderStatus(orderStatus);

        return orderMapper.toOrdersResponse(ordersByStatus);
    }

    @Override
    @Transactional
    public void autoRateOrders() {
        LocalDateTime now = LocalDateTime.now();
        // Retrieve orders with the specified order status from the order details repository
        List<Order> orders = orderRepository.findOrderDetailsByOrderStatus(OrderStatus.RECEIVED);
        try {
            for (Order order : orders) {
                if (!order.getIsBuyerRate()
                        && now.isAfter(
                                convertToLocalDateTime(order.getOrderDetails().getReceivedDate())
                                        .plusDays(3))) {
                    autoRateOrder(order);
                    order.setIsBuyerRate(true);

                    // TODO: notify to seller that buyer has auto rate
                    notificationService.createNotificationStorage(NotificationEntity.builder()
                            .delivered(false)
                            .message(order.getBuyer().getUsername()
                                    + " đã đánh giá đơn hàng của bạn! Đánh giá lại ngay! ")
                            .notificationType(NotificationType.RATE)
                            .userFromId(order.getBuyer().getId())
                            .timestamp(new Date())
                            .userToId(order.getBuyer().getId())
                            .userFromAvatar(order.getBuyer().getAvatar())
                            .orderId(order.getId())
                            .build());
                    //                 completeOrder(order);
                }
            }
        } catch (Exception e) {
            log.error("An error occurred while create auto rating : {}", e.getMessage());
        }
    }

    @Transactional
    protected void autoRateOrder(Order order) {
        try {
            RatingRequest ratingRequest = new RatingRequest();
            ratingRequest.setStars(StarRating.FIVE_STAR);
            ratingRequest.setComment("");
            ratingRequest.setRatingUserId(order.getBuyer().getId());
            ratingRequest.setRatedUserId(order.getPost().getUser().getId());
            orderRepository.save(order);
        } catch (Exception e) {
            log.error("An error occurred while create rating : {}", e.getMessage());
        }
    }

    @Transactional
    protected void completeOrder(Order order) {
        try {
            if (order.getIsBuyerRate() && order.getIsSellerRate()) {
                order.getOrderDetails().setStatus(OrderStatus.COMPLETED);
                orderRepository.save(order);
            }
        } catch (Exception e) {
            log.error("An error occurred when trying to complete the order: {}", e.getMessage());
        }
    }

    private static LocalDateTime convertToLocalDateTime(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }
}
