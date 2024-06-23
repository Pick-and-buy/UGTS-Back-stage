package com.ugts.order.dto.request;

import com.ugts.order.entity.OrderDetails;
import com.ugts.product.entity.Product;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class CreateOrderRequest {
    Product product;
    OrderDetails orderDetails;
}
