package com.ugts.wallet.service;

public interface IWalletService {
    Double showBalance(String userId);

    void fundTransfer(String fromUserId, String toUserId, Double amount);

    void depositMoney(String userId, Double amount);

    void withdrawMoney(String userId, Double amount);
}
