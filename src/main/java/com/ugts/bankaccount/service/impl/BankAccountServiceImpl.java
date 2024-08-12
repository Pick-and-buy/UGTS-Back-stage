package com.ugts.bankaccount.service.impl;

import com.ugts.bankaccount.dto.BankAccountRequest;
import com.ugts.bankaccount.entity.BankAccount;
import com.ugts.bankaccount.repository.BankAccountRepository;
import com.ugts.bankaccount.service.IBankAccountService;
import com.ugts.common.exception.AppException;
import com.ugts.common.exception.ErrorCode;
import com.ugts.user.entity.User;
import com.ugts.user.repository.UserRepository;
import com.ugts.wallet.entity.Wallet;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BankAccountServiceImpl implements IBankAccountService {
    private final UserRepository userRepository;
    private final BankAccountRepository bankAccountRepository;

    @Override
    public void addBankAccount(BankAccountRequest bankAccountRequest, String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        Wallet wallet = user.getWallet();
        if (bankAccountRepository.findByBankBin(bankAccountRequest.getBankBin()) != null) {
            throw new AppException(ErrorCode.BANK_ACCOUNT_ALREADY_EXISTED);
        }

        BankAccount bankAccount = new BankAccount();
        bankAccount.setBankName(bankAccountRequest.getBankName());
        bankAccount.setBankId(bankAccountRequest.getBankId());
        bankAccount.setBankBin(bankAccountRequest.getBankBin());
        bankAccount.setBankShortName(bankAccountRequest.getBankShortName());
        bankAccount.setBankCode(bankAccountRequest.getBankCode());
        bankAccount.setBankBalance(0.0);
        bankAccount.setWalletId(wallet.getWalletId());
        bankAccountRepository.save(bankAccount);
    }
}
