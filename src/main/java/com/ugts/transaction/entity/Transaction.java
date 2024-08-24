package com.ugts.transaction.entity;

import java.time.LocalDateTime;

import com.ugts.order.entity.Order;
import com.ugts.transaction.enums.TransactionStatus;
import com.ugts.transaction.enums.TransactionType;
import com.ugts.user.entity.User;
import com.ugts.wallet.entity.Wallet;
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

    String billNo;

    String transNo;

    String bankCode;

    String cardType;

    double amount;

    String currency;

    String bankAccountNo;

    String bankAccount;

    String refundBankCode;

    @Column
    private String reason;

    LocalDateTime createDate;

    @Enumerated(EnumType.STRING)
    TransactionStatus transactionStatus;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    User user;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    Order order;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    Wallet wallet;

    @Enumerated(EnumType.STRING)
    TransactionType transactionType;
}
