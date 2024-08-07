package com.ugts.wallet.service;

import com.ugts.wallet.dto.WalletResponse;

public interface IWalletService {
    WalletResponse registerNewWallet();

    double charge(String walletId, double amount);

    WalletResponse getWalletInformation(String walletId);

    void fundTransfer(String fromUserId, String toUserId, Double amount);

    void depositMoney(String userId, Double amount);

    void withdrawMoney(String userId, Double amount);
}
