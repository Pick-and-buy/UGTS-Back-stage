package com.ugts.order.mapper;

import java.util.List;

import com.ugts.order.dto.response.OrderResponse;
import com.ugts.order.entity.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderResponse toOrderResponse(Order order);

    List<OrderResponse> toOrdersResponse(List<Order> order);
}
