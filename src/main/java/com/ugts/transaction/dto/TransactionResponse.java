package com.ugts.transaction.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ugts.order.dto.response.OrderResponse;
import com.ugts.transaction.enums.TransactionStatus;
import com.ugts.user.dto.GeneralUserInformationDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    Double amount;
    String currency;
    String bankAccountNo;
    String bankAccount;
    String refundBankCode;
    String reason;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yy-MM-dd hh:mm:ss")
    Date createDate;

    TransactionStatus transactionStatus;
    GeneralUserInformationDto user;
    OrderResponse order;
}
