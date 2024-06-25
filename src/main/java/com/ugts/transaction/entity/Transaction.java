package com.ugts.transaction.entity;

import java.util.Date;

import com.ugts.order.entity.Order;
import com.ugts.transaction.enums.TransactionStatus;
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
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    //    @GeneratedValue(strategy = GenerationType.UUID)
    String billNo;

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
    TransactionStatus transactionStatus;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    User user;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    Order order;
}