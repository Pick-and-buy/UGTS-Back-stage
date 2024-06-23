package com.ugts.order.entity;

import com.ugts.transaction.entity.Transaction;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.List;

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

    @OneToOne(fetch = FetchType.LAZY)
    Order order;

    double price;

    String shippingAddress;

    @Column(nullable = false, length = 50)
    String paymentMethod;

    Boolean isPaid = false;

    Boolean isRefund;

    Date orderDate;

    Date packageDate;

    Date deliveryDate;

    Date receivedDate;
}
