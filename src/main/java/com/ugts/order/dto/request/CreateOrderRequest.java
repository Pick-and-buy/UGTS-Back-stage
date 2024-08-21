package com.ugts.order.dto.request;

import java.util.Date;

import com.ugts.post.entity.Post;
import com.ugts.user.entity.Address;
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
    Address address;
    Date packageDate;
    Date deliveryDate;
    Date receivedDate;
    Post post;
    String shippingCost;
}
