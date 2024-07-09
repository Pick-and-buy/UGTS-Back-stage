package com.ugts.order.dto.response;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ugts.order.enums.OrderStatus;
import com.ugts.user.entity.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailsResponse {
    Long id;
    double price;
    int quantity;
    boolean isFeedBack;
    String firstName;
    String lastName;
    String email;
    String phoneNumber;
    Address address;
    String paymentMethod;
    OrderStatus status;
    Boolean isPaid;
    Boolean isRefund;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yy-MM-dd hh:mm:ss")
    Date orderDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yy-MM-dd hh:mm:ss")
    Date packageDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yy-MM-dd hh:mm:ss")
    Date deliveryDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yy-MM-dd hh:mm:ss")
    Date receivedDate;
}
