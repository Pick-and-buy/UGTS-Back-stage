package com.ugts.bankaccount;

import lombok.Data;

@Data
public class BankAccountRequest {
    private String bankName;
    private Integer bankId;
    private String bankBin;
    private String bankShortName;
    private String bankCode;
    private String walletId;
    private Double bankBalance;
}
