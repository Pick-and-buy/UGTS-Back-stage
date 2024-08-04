package com.ugts.vnpay.controller;

import com.ugts.dto.ApiResponse;
import com.ugts.transaction.repository.TransactionRepository;
import com.ugts.user.repository.UserRepository;
import com.ugts.user.service.UserService;
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

    TransactionRepository transactionRepository;

    UserRepository userRepository;

    UserService userService;

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
    public ApiResponse<String> getPaymentInfo(HttpServletRequest request, String orderId) {
        var result = vnPayService.getPaymentInfo(request, orderId);
        return ApiResponse.<String>builder().message("Success").result(result).build();
    }
}
