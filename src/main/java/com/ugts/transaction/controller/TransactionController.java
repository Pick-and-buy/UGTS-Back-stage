package com.ugts.transaction.controller;

import com.ugts.dto.ApiResponse;
import com.ugts.transaction.dto.TransactionRequest;
import com.ugts.transaction.dto.TransactionResponse;
import com.ugts.transaction.service.TransactionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/transactions")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TransactionController {

    TransactionService transactionService;

    @PostMapping
    public ApiResponse<TransactionResponse> createTransaction(
            @RequestBody TransactionRequest transactionRequest, @RequestParam String orderId) {
        var result = transactionService.createTransaction(transactionRequest, orderId);
        return ApiResponse.<TransactionResponse>builder()
                .message("Create transaction success")
                .result(result)
                .build();
    }
}
