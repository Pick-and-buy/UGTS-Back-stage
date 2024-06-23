package com.ugts.order.dto.response;

import com.ugts.order.entity.OrderDetails;
import com.ugts.product.entity.Product;
import com.ugts.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    String id;
    User user;
    Product product;
    OrderDetails orderDetails;
    String status;
}
