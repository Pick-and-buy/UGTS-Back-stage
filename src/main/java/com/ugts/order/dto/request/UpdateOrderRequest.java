package com.ugts.order.dto.request;

import java.util.Date;

import com.ugts.order.enums.OrderStatus;
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
public class UpdateOrderRequest {
    OrderStatus orderStatus;
    String firstName;
    String lastName;
    String email;
    String phoneNumber;
    Address address;
    String paymentMethod;
    Date packageDate;
    Date deliveryDate;
    Date receivedDate;
    Post post;
}
