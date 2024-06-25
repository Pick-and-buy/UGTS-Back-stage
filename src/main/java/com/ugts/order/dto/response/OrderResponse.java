package com.ugts.order.dto.response;

import java.util.List;

import com.ugts.order.entity.OrderDetails;
import com.ugts.post.dto.response.PostResponse;
import com.ugts.user.dto.response.UserResponse;
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
    UserResponse buyer;
    PostResponse post;
    List<OrderDetails> orderDetails;
}
