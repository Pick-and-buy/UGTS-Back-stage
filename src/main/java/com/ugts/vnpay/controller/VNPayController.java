package com.ugts.vnpay.controller;

import com.ugts.common.dto.ApiResponse;
import com.ugts.transaction.dto.TransactionResponse;
import com.ugts.vnpay.service.VNPayService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/vnpay")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class VNPayController {

    VNPayService vnPayService;

    @PostMapping(value = "/create-payment", produces = "application/json;charset=UTF-8")
    public ApiResponse<String> CreatePayment(
            @RequestParam("amount") int orderTotal,
            @RequestParam("orderInfo") String orderInfo,
            HttpServletRequest request) {
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        var result = vnPayService.createPayment(orderTotal, orderInfo, baseUrl);
        return ApiResponse.<String>builder()
                .message("Create Payment Success")
                .result(result)
                .build();
    }

    @GetMapping("/payment-info")
    public ApiResponse<TransactionResponse> getPaymentInfo(HttpServletRequest request) {
        return vnPayService.getPaymentInfo(request);
    }
}
