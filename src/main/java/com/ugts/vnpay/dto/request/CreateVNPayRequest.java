package com.ugts.vnpay.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateVNPayRequest {
    private Integer amount;
    private String reason;
}
