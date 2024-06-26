package com.ugts.order.dto.request;

import java.util.Date;

import com.ugts.order.enums.OrderStatus;
import com.ugts.post.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateOrderRequest {
    OrderStatus orderStatus;
    String paymentMethod;
    Date packageDate;
    Date deliveryDate;
    Date receivedDate;
    Post post;
}
