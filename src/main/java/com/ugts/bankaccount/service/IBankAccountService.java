package com.ugts.bankaccount.service;

import com.ugts.bankaccount.dto.BankAccountRequest;

public interface IBankAccountService {
    void addBankAccount(BankAccountRequest bankAccountRequest, String userId);
}
