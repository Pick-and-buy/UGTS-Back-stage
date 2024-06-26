package com.ugts.order.service;

import com.ugts.order.dto.request.CreateOrderRequest;
import com.ugts.order.dto.response.OrderResponse;

public interface OrderService {

    OrderResponse createOrder(CreateOrderRequest orderRequest);
}
