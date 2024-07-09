package com.ugts.vnpay.service;

import jakarta.servlet.http.HttpServletRequest;

public interface VNPayService {
    String createOrder(int total, String reason, String urlReturn, String ipAddr);

    int orderReturn(HttpServletRequest request);
}
