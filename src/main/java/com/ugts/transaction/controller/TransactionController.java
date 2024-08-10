package com.ugts.transaction.controller;

import com.ugts.dto.ApiResponse;
import com.ugts.transaction.dto.TransactionResponse;
import com.ugts.transaction.service.TransactionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/transactions")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TransactionController {

    TransactionService transactionService;

    @GetMapping
    public ApiResponse<List<TransactionResponse>> getAllTransactions() {
        var result = transactionService.getAllTransactions();
        return ApiResponse.<List<TransactionResponse>>builder()
                .result(result)
                .build();
    }

    @GetMapping("/user")
    public ApiResponse<List<TransactionResponse>> getTransactionsByUserId(@RequestParam("userId") String userId) {
        var result = transactionService.getTransactionsByUserId(userId);
        return ApiResponse.<List<TransactionResponse>>builder()
                .result(result)
                .build();
    }

    @GetMapping("/id")
    public ApiResponse<TransactionResponse> getTransactionById(@RequestParam("transactionId") String transactionId) {
        var result = transactionService.getTransactionById(transactionId);
        return ApiResponse.<TransactionResponse>builder()
                .result(result)
                .build();
    }
}
