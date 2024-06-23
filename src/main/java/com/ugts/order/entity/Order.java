package com.ugts.order.entity;

import com.ugts.order.enums.OrderStatus;
import com.ugts.product.entity.Product;
import com.ugts.transaction.entity.Transaction;
import com.ugts.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "`order`")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    User user;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    Product product;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    OrderDetails orderDetails;

    @Enumerated(EnumType.STRING)
    OrderStatus status;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "order")
    List<Transaction> transactions;
}
