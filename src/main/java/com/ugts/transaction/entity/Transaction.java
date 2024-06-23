package com.ugts.transaction.entity;

import com.ugts.order.entity.Order;
import com.ugts.order.entity.OrderDetails;
import com.ugts.transaction.enums.TransactionStatus;
import com.ugts.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    String billNo;

    String transNo;

    String bankCode;

    String cardType;

    Integer amount;

    String currency;

    String bankAccountNo;

    String bankAccount;

    String refundBankCode;

    @Lob
    @Column
    private String reason;

    Date createDate;

    @Enumerated(EnumType.STRING)
    TransactionStatus status;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    User user;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    Order order;
}
