package com.ugts.order.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ugts.order.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    double price;

    int quantity;

    boolean isFeedBack;

    String firstName;

    String lastName;

    String email;

    String phoneNumber;

    String address;

    @Column(nullable = false, length = 50)
    String paymentMethod;

    @Enumerated(EnumType.STRING)
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

    @ManyToOne(fetch = FetchType.LAZY)
    Order order;
}
