package com.ugts.transaction.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ugts.order.dto.response.OrderResponse;
import com.ugts.user.dto.response.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponse {
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
    String reason;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yy-MM-dd hh:mm:ss")
    Date createDate;
    UserResponse user;
    OrderResponse order;
}
