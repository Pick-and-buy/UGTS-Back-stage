package com.ugts.order.dto.response;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ugts.order.enums.OrderStatus;
import com.ugts.user.dto.response.AddressResponse;
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
    AddressResponse address;
    String paymentMethod;
    OrderStatus status;
    Boolean isPaid;
    Boolean isRefund;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yy-MM-dd hh:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    Date orderDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yy-MM-dd hh:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    Date packageDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yy-MM-dd hh:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    Date deliveryDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yy-MM-dd hh:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    Date receivedDate;
}
