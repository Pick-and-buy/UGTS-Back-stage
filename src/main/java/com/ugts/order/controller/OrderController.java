package com.ugts.order.controller;

import java.io.IOException;
import java.util.List;

import com.ugts.common.dto.ApiResponse;
import com.ugts.order.dto.request.CreateOrderRequest;
import com.ugts.order.dto.request.UpdateOrderRequest;
import com.ugts.order.dto.response.OrderResponse;
import com.ugts.order.enums.OrderStatus;
import com.ugts.order.service.OrderService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderController {
    OrderService orderService;

    @PostMapping
    public ApiResponse<OrderResponse> createOrder(@RequestBody CreateOrderRequest orderRequest) {
        var result = orderService.createOrder(orderRequest);
        return ApiResponse.<OrderResponse>builder()
                .message("Create order success")
                .result(result)
                .build();
    }

    @PutMapping
    public ApiResponse<OrderResponse> updateOrderStatus(
            @RequestParam String orderId, @RequestBody UpdateOrderRequest orderRequest) {
        var result = orderService.updateOrderStatus(orderId, orderRequest);
        return ApiResponse.<OrderResponse>builder()
                .message("Update order status success")
                .result(result)
                .build();
    }

    @PutMapping("/details")
    public ApiResponse<OrderResponse> updateOrderDetails(
            @RequestParam String orderId, @RequestBody UpdateOrderRequest orderRequest) {
        var result = orderService.updateOrderDetails(orderId, orderRequest);
        return ApiResponse.<OrderResponse>builder()
                .message("Update order details success")
                .result(result)
                .build();
    }

    @PutMapping("/package-video")
    public ApiResponse<Void> updateOrderVideo(
            @RequestParam String orderId,
            @RequestPart(value = "productVideo", required = false) MultipartFile productVideo)
            throws IOException {
        orderService.updateVideoOrder(orderId, productVideo);
        return ApiResponse.<Void>builder().message("Update order video success").build();
    }

    @GetMapping
    public ApiResponse<List<OrderResponse>> getAllOrders() {
        var result = orderService.getAllOrders();
        return ApiResponse.<List<OrderResponse>>builder()
                .message("Success")
                .result(result)
                .build();
    }

    @GetMapping("/details")
    public ApiResponse<OrderResponse> getOrderByOrderId(String orderId) {
        var result = orderService.getOrderByOrderId(orderId);
        return ApiResponse.<OrderResponse>builder()
                .message("Success")
                .result(result)
                .build();
    }

    @GetMapping("/status")
    public ApiResponse<List<OrderResponse>> getOrderByOrderStatus(@RequestParam OrderStatus orderStatus) {
        var result = orderService.getOrderByOrderStatus(orderStatus);
        return ApiResponse.<List<OrderResponse>>builder()
                .message("Success")
                .result(result)
                .build();
    }

    @PutMapping("/status-admin")
    public ApiResponse<OrderResponse> updateOrderStatusForAdmin(
            @RequestParam String orderId, @RequestParam OrderStatus orderStatus) {
        var result = orderService.updateOrderStatusAdmin(orderId, orderStatus);
        return ApiResponse.<OrderResponse>builder()
                .message("Update order status success")
                .result(result)
                .build();
    }
}
