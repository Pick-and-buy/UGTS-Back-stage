package com.ugts.transaction.dto;

import com.ugts.order.entity.Order;
import com.ugts.transaction.enums.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRequest {
    String bankCode;
    String cardType;
    Integer amount;
    String currency;
    String bankAccountNo;
    String bankAccount;
    String refundBankCode;
    String reason;
    TransactionStatus transactionStatus;
    Order order;
}
