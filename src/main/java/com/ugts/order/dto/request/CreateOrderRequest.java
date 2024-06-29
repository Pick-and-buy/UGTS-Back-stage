package com.ugts.order.dto.request;

import java.util.Date;
import java.util.List;

import com.ugts.order.entity.OrderDetails;
import com.ugts.post.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderRequest {
    String paymentMethod;
    Date packageDate;
    Date deliveryDate;
    Date receivedDate;
    Post post;
    List<OrderDetails> orderDetails;
}
