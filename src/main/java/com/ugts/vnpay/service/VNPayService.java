package com.ugts.vnpay.service;

import jakarta.servlet.http.HttpServletRequest;

public interface VNPayService {
    String createPayment(int total, String orderInfo, String urlReturn);

    int orderReturn(HttpServletRequest request);

    String getPaymentInfo(HttpServletRequest request, String orderId);
}
