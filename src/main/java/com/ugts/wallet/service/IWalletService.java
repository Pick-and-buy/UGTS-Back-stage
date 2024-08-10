package com.ugts.wallet.service;

import java.util.List;

import com.ugts.transaction.dto.TransactionResponse;
import com.ugts.wallet.dto.WalletResponse;

public interface IWalletService {
    WalletResponse registerNewWallet();

    double charge(String walletId, double amount);

    WalletResponse getWalletInformation(String walletId);

    double payForOrder(String walletId, String order, double payAmount);

    List<TransactionResponse> getTransactionHistories();

    void fundTransfer(String fromUserId, String toUserId, Double amount);

    void withdrawMoney(String userId, Double amount);
}
