package com.ugts.wallet.controller;

import com.ugts.dto.ApiResponse;
import com.ugts.wallet.dto.WalletResponse;
import com.ugts.wallet.service.IWalletService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    public ApiResponse<Void> charge(@RequestParam("walletId") String walletId, @RequestParam("amount") double amount) {
        walletService.charge(walletId, amount);
        return ApiResponse.<Void>builder().message("Charge success").build();
    }
}
