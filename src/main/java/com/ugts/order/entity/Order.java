package com.ugts.order.entity;

import java.util.ArrayList;
import java.util.List;

import com.ugts.post.entity.Post;
import com.ugts.transaction.entity.Transaction;
import com.ugts.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

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

    @ManyToOne(fetch = FetchType.LAZY)
    User buyer;

    @OneToOne(fetch = FetchType.LAZY)
    Post post;

    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    List<OrderDetails> orderDetails = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "order")
    List<Transaction> transactions = new ArrayList<>();
}
