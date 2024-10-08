package com.ugts.order.service.impl;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import com.ugts.common.cloudService.GoogleCloudStorageService;
import com.ugts.common.exception.AppException;
import com.ugts.common.exception.ErrorCode;
import com.ugts.notification.entity.NotificationEntity;
import com.ugts.notification.entity.NotificationType;
import com.ugts.notification.service.impl.NotificationServiceImpl;
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
import com.ugts.transaction.entity.Transaction;
import com.ugts.transaction.enums.TransactionStatus;
import com.ugts.transaction.enums.TransactionType;
import com.ugts.transaction.repository.TransactionRepository;
import com.ugts.user.entity.Address;
import com.ugts.user.entity.User;
import com.ugts.user.repository.AddressRepository;
import com.ugts.user.repository.UserRepository;
import com.ugts.user.service.UserService;
import com.ugts.wallet.entity.Wallet;
import com.ugts.wallet.repository.WalletRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

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

    private final NotificationServiceImpl notificationService;

    GoogleCloudStorageService googleCloudStorageService;

    WalletRepository walletRepository;

    TransactionRepository transactionRepository;


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
                .lastPriceForSeller(post.getProduct().getPrice())
                .lastPriceForBuyer(Double.parseDouble(orderRequest.getShippingCost()) + post.getProduct().getPrice())
                .shippingCost(Double.valueOf(orderRequest.getShippingCost()))
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
    @Modifying
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
        var user = getUserUpdateOrder();

        // Verify that the user is authorized to update the order
        if (!Objects.equals(seller.getId(), user.getId())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        var orderDetails = order.getOrderDetails();

        String buyerWalletId = order.getBuyer().getWallet().getWalletId();
        var buyerWallet =
                walletRepository.findById(buyerWalletId).orElseThrow(() -> new AppException(ErrorCode.WALLET_NOT_FOUND));

        if (orderRequest.getOrderStatus() == OrderStatus.CANCELLED) {
            post.setIsAvailable(true);
            orderDetails.setStatus(OrderStatus.CANCELLED);

            refundForBuyer(order, user, orderDetails, buyerWallet);

            postRepository.save(post);
        }

        orderDetails.setStatus(orderRequest.getOrderStatus());
        orderDetailsRepository.save(orderDetails);

        return orderMapper.toOrderResponse(orderRepository.save(order));
    }

    protected User getUserUpdateOrder(){
        var contextHolder = SecurityContextHolder.getContext();
        String phoneNumber = contextHolder.getAuthentication().getName();

        return userRepository
                .findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
    }
    //buyer
    @Override
    @Transactional
    @Modifying
    @PreAuthorize("hasAnyRole('USER')")
    public OrderResponse updateOrderDetails(String orderId, UpdateOrderRequest updateOrderRequest) {
        // Get the order
        var order = orderRepository.findById(orderId).orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        // Get the post associated with the order
        var post = postRepository
                .findById(order.getPost().getId())
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_EXISTED));
        // Get the user who created the order
        var buyer = userRepository
                .findById(order.getBuyer().getId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        // Get user who is updating the order
        var user = getUserUpdateOrder();

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

        var orderDetails = order.getOrderDetails();
        orderDetails.setFirstName(updateOrderRequest.getFirstName());
        orderDetails.setLastName(updateOrderRequest.getLastName());
        orderDetails.setEmail(updateOrderRequest.getEmail());
        orderDetails.setPhoneNumber(updateOrderRequest.getPhoneNumber());
        orderDetails.setAddress(address);
        orderDetails.setPaymentMethod(order.getOrderDetails().getPaymentMethod());
        orderDetails.setLastPriceForSeller(post.getLastPriceForSeller());

        if (updateOrderRequest.getOrderStatus() == OrderStatus.CANCELLED) {
            orderDetails.setStatus(OrderStatus.CANCELLED);
            orderDetailsRepository.save(orderDetails);

            String buyerWalletId = buyer.getWallet().getWalletId();
            var buyerWallet =
                    walletRepository.findById(buyerWalletId).orElseThrow(() -> new AppException(ErrorCode.WALLET_NOT_FOUND));

            refundForBuyer(order, user, orderDetails, buyerWallet);

            post.setIsAvailable(true);
            postRepository.save(post);
        }
        return orderMapper.toOrderResponse(orderRepository.save(order));
    }

    @Transactional
    @Modifying
    protected void refundForBuyer(Order order, User user, OrderDetails orderDetails, Wallet buyerWallet) {
        var currentBalance = buyerWallet.getBalance();
        double newBalance = currentBalance + orderDetails.getLastPriceForBuyer();
        buyerWallet.setBalance(newBalance);

        var transaction = Transaction.builder()
                .amount(orderDetails.getLastPriceForBuyer())
                .currency("VND")
                .reason("Refund for buyer cancelled order")
                .createDate(LocalDate.now())
                .transactionStatus(TransactionStatus.SUCCESS)
                .user(user)
                .order(order)
                .wallet(buyerWallet)
                .transactionType(TransactionType.REFUND)
                .build();
        transactionRepository.save(transaction);
        walletRepository.save(buyerWallet);
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

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public List<OrderResponse> getAllOrdersForAdmin() {
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
                }
            }
        } catch (Exception e) {
            log.error("An error occurred while create auto rating : {}", e.getMessage());
        }
    }

    /**
     * @param orderId
     * @param orderStatus
     * @return
     */
    @Override
    @Modifying
    @PreAuthorize("hasRole('ADMIN')")
    public OrderResponse updateOrderStatusAdmin(String orderId, OrderStatus orderStatus) {
        var order = orderRepository.findById(orderId).orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        try{
            order.getOrderDetails().setStatus(orderStatus);
            orderRepository.save(order);
            return orderMapper.toOrderResponse(orderRepository.save(order));

        } catch (Exception e) {
            log.error("An error occurred while update order status : {}", e.getMessage());
        }
        return orderMapper.toOrderResponse(orderRepository.save(order));
    }

    /**
     * @param orderId
     * @param productVideo
     * @return
     */
    @Override
    @Transactional
    @Modifying
    public void updateVideoOrder(String orderId, MultipartFile productVideo) throws IOException {
        // Get the order
        var order = orderRepository.findById(orderId).orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

        // Get the user who created the order
        var buyer = userRepository
                .findById(order.getBuyer().getId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        if(productVideo != null) {
            // Get user who is updating the order
            var user = userService.getProfile();

            // seller update packing video
            if (Objects.equals(order.getPost().getUser().getId(), user.getId())) {
                String sellerVideoUrl = googleCloudStorageService.uploadOrderVideoToGCS(productVideo, order.getId());
                order.getOrderDetails().setPackingVideo(sellerVideoUrl);
                orderRepository.save(order);
            }

            // buyer update receive video
            if (Objects.equals(buyer.getId(), user.getId())) {
                String buyerVideoUrl = googleCloudStorageService.uploadOrderVideoToGCS(productVideo, order.getId());
                order.getOrderDetails().setReceivePackageVideo(buyerVideoUrl);
                orderRepository.save(order);
            }
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

    private static LocalDateTime convertToLocalDateTime(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }
}
