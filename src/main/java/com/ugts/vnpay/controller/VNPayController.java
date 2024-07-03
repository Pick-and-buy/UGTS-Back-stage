package com.ugts.vnpay.controller;

import com.ugts.exception.AppException;
import com.ugts.exception.ErrorCode;
import com.ugts.transaction.entity.Transaction;
import com.ugts.transaction.enums.TransactionStatus;
import com.ugts.transaction.repository.TransactionRepository;
import com.ugts.user.repository.UserRepository;
import com.ugts.vnpay.dto.request.CreateVNPayRequest;
import com.ugts.vnpay.service.VNPayService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/vnpay")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class VNPayController {

    VNPayService vnPayService;

    TransactionRepository transactionRepository;

    UserRepository userRepository;

    @PostMapping(value = "/submitOrder", produces = "application/json;charset=UTF-8")
    public String submitOrder(@RequestBody CreateVNPayRequest createVNPayRequest,
                              HttpServletRequest request) {
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        return vnPayService.createOrder(createVNPayRequest.getAmount(), createVNPayRequest.getReason(), baseUrl, request.getRemoteAddr());
    }

    @GetMapping("/getPaymentInfo")
    public ResponseEntity<?> getPaymentInfo(HttpServletRequest request) {
        int paymentStatus = vnPayService.orderReturn(request);

        Transaction transaction = new Transaction();
        transaction.setId(request.getParameter("vnp_TransactionNo"));
        transaction.setBillNo(request.getParameter("vnp_TxnRef"));
        transaction.setTransNo(request.getParameter("vnp_TransactionNo"));
        transaction.setBankCode(request.getParameter("vnp_BankCode"));
        transaction.setCardType(request.getParameter("vnp_CardType"));
        transaction.setAmount(Integer.parseInt(request.getParameter("vnp_Amount")));
        transaction.setCurrency("VND");
        transaction.setCreateDate(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
        String orderInfo = request.getParameter("vnp_OrderInfo");
        String[] arrayInfo = orderInfo.split("-");

        var user = userRepository.findById(arrayInfo[0])
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        String reason = arrayInfo[1];
        transaction.setUser(user);
        transaction.setReason(reason);
        if(paymentStatus == 1) {
            transaction.setTransactionStatus(TransactionStatus.SUCCESS);
            transactionRepository.save(transaction);
            return ResponseEntity.ok().body("");
        } else if(paymentStatus == 0) {
            transaction.setTransactionStatus(TransactionStatus.FAILED);
            transactionRepository.save(transaction);
            return ResponseEntity.badRequest().body("");
        } else {
            return ResponseEntity.badRequest().body("Lỗi !!! Mã Secure Hash không hợp lệ.");
        }
    }
}
