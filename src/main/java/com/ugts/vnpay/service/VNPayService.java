package com.ugts.vnpay.service;

import com.ugts.common.dto.ApiResponse;
import com.ugts.transaction.dto.TransactionResponse;
import jakarta.servlet.http.HttpServletRequest;

public interface VNPayService {
    String createPayment(int total, String orderInfo, String urlReturn);

    int orderReturn(HttpServletRequest request);

    ApiResponse<TransactionResponse> getPaymentInfo(HttpServletRequest request);
}
