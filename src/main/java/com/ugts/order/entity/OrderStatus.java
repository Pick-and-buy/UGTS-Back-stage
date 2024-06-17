package com.ugts.order.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum OrderStatus {
    PENDING("The order has been created but not yet processed."),
    PROCESSING("The order is currently being processed."),
    DELIVERED("The order has been delivered to the customer."),
    CANCELLED("The order has been cancelled."),
    RETURNED("The order has been returned by the customer.");

    String description;
}
