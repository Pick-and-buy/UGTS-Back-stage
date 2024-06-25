package com.ugts.order.dto.response;

import java.util.List;

import com.ugts.order.entity.OrderDetails;
import com.ugts.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    String id;
    User user;
    List<OrderDetails> orderDetails;
}
