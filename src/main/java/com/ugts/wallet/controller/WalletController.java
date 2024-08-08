package com.ugts.wallet.controller;

import java.util.Set;

import com.ugts.dto.ApiResponse;
import com.ugts.transaction.dto.TransactionResponse;
import com.ugts.wallet.dto.WalletResponse;
import com.ugts.wallet.service.IWalletService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/wallets")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WalletController {
    IWalletService walletService;

    /**
     * Registers a new wallet and returns an API response with the success message and the created wallet response.
     *
     * @return ApiResponse with the success message and the newly created WalletResponse
     */
    @PostMapping("/register")
    public ApiResponse<WalletResponse> registerNewWallet() {
        var result = walletService.registerNewWallet();
        return ApiResponse.<WalletResponse>builder()
                .message("Register new wallet success")
                .result(result)
                .build();
    }

    /**
     * Charges the specified wallet with the given amount and returns an API response indicating the success of the charge operation.
     *
     * @param walletId The ID of the wallet to be charged
     * @param amount   The amount to be charged to the wallet
     * @return ApiResponse containing the success message and the charged amount
     */
    @PostMapping("/charge")
    public ApiResponse<Double> charge(
            @RequestParam("walletId") String walletId, @RequestParam("amount") double amount) {
        var result = walletService.charge(walletId, amount);
        return ApiResponse.<Double>builder()
                .message("Charge success")
                .result(result)
                .build();
    }

    /**
     * Retrieves and returns the wallet information for the specified wallet ID.
     *
     * @param walletId The ID of the wallet to retrieve information for
     * @return ApiResponse containing the success message and the retrieved WalletResponse
     */
    @GetMapping("/info")
    public ApiResponse<WalletResponse> getWalletInfo(@RequestParam("walletId") String walletId) {
        var result = walletService.getWalletInformation(walletId);
        return ApiResponse.<WalletResponse>builder()
                .message("Success")
                .result(result)
                .build();
    }

    /**
     * Executes a payment for a specific order using the provided wallet ID, order ID, and payment amount.
     *
     * @param walletId The ID of the wallet making the payment
     * @param orderId The ID of the order being paid for
     * @param payAmount The amount to be paid for the order
     * @return ApiResponse containing a success message and the payment result
     */
    @PutMapping("/pay-order")
    public ApiResponse<Double> payOrder(
            @RequestParam("walletId") String walletId,
            @RequestParam("orderId") String orderId,
            @RequestParam("payAmount") double payAmount) {
        var result = walletService.payForOrder(walletId, orderId, payAmount);
        return ApiResponse.<Double>builder()
                .message("Pay Success")
                .result(result)
                .build();
    }

    /**
     * Retrieves and returns the transaction histories associated with wallets.
     *
     * @return ApiResponse containing a success message and the set of TransactionResponse objects
     */
    @GetMapping("/transaction-histories")
    public ApiResponse<Set<TransactionResponse>> getTransactionHistories() {
        var result = walletService.getTransactionHistories();
        return ApiResponse.<Set<TransactionResponse>>builder()
                .message("Success")
                .result(result)
                .build();
    }
}
