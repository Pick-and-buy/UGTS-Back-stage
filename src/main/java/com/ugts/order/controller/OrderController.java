package com.ugts.order.controller;

import com.ugts.dto.ApiResponse;
import com.ugts.order.dto.request.CreateOrderRequest;
import com.ugts.order.dto.request.UpdateOrderRequest;
import com.ugts.order.dto.response.OrderResponse;
import com.ugts.order.service.OrderService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

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
}
