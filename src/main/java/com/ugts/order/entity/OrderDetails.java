package com.ugts.order.entity;

import java.util.Date;

import com.ugts.order.enums.OrderStatus;
import com.ugts.user.entity.Address;
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

    @OneToOne(fetch = FetchType.LAZY)
    Address address;

    @Column(nullable = false, length = 50)
    String paymentMethod;

    @Enumerated(EnumType.STRING)
    OrderStatus status;

    Boolean isPaid;

    Boolean isRefund;

    Date orderDate;

    Date packageDate;

    Date deliveryDate;

    Date receivedDate;

    @OneToOne(fetch = FetchType.LAZY)
    Order order;
}
