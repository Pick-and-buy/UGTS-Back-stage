package com.ugts.order.service;

import java.io.IOException;
import java.util.List;

import com.ugts.order.dto.request.CreateOrderRequest;
import com.ugts.order.dto.request.UpdateOrderRequest;
import com.ugts.order.dto.response.OrderResponse;
import com.ugts.order.enums.OrderStatus;
import org.springframework.web.multipart.MultipartFile;

public interface OrderService {

    OrderResponse createOrder(CreateOrderRequest orderRequest);

    OrderResponse updateOrderStatus(String orderId, UpdateOrderRequest orderRequest);

    OrderResponse updateOrderDetails(String orderId, UpdateOrderRequest orderRequest);

    List<OrderResponse> getAllOrders();

    OrderResponse getOrderByOrderId(String orderId);

    List<OrderResponse> getOrderByOrderStatus(OrderStatus orderStatus);

    void autoRateOrders();

    OrderResponse updateOrderStatusAdmin(String orderId, OrderStatus orderStatus);

    void updateVideoOrder(String orderId, MultipartFile productVideo) throws IOException;
}
