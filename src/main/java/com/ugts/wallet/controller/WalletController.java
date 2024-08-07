package com.ugts.wallet.controller;

import com.ugts.dto.ApiResponse;
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

    @PostMapping("/register")
    public ApiResponse<WalletResponse> registerNewWallet() {
        var result = walletService.registerNewWallet();
        return ApiResponse.<WalletResponse>builder()
                .message("Register new wallet success")
                .result(result)
                .build();
    }

    @PostMapping("/charge")
    public ApiResponse<Double> charge(
            @RequestParam("walletId") String walletId, @RequestParam("amount") double amount) {
        var result = walletService.charge(walletId, amount);
        return ApiResponse.<Double>builder()
                .message("Charge success")
                .result(result)
                .build();
    }

    @GetMapping("/info")
    public ApiResponse<WalletResponse> getWalletInfo(@RequestParam("walletId") String walletId) {
        var result = walletService.getWalletInformation(walletId);
        return ApiResponse.<WalletResponse>builder()
                .message("Success")
                .result(result)
                .build();
    }
}
