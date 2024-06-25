package com.ugts.order.dto.request;

import java.util.List;

import com.ugts.order.entity.OrderDetails;
import com.ugts.order.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderRequest {
    String firstName;
    String lastName;
    String email;
    String phoneNumber;
    String address;
    String paymentMethod;
    OrderStatus status;
    List<OrderDetails> orderDetails;
}
