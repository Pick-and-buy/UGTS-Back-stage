package com.ugts.wallet.service.impl;

import java.time.LocalDateTime;

import com.ugts.bankaccount.BankAccount;
import com.ugts.bankaccount.BankAccountRepository;
import com.ugts.exception.AppException;
import com.ugts.exception.ErrorCode;
import com.ugts.transaction.entity.Transaction;
import com.ugts.transaction.enums.TransactionType;
import com.ugts.transaction.repository.TransactionRepository;
import com.ugts.user.entity.User;
import com.ugts.user.repository.UserRepository;
import com.ugts.user.service.UserService;
import com.ugts.wallet.dto.WalletResponse;
import com.ugts.wallet.entity.Wallet;
import com.ugts.wallet.mapper.WalletMapper;
import com.ugts.wallet.repository.WalletRepository;
import com.ugts.wallet.service.IWalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements IWalletService {
    private final UserRepository userRepository;
    private final BankAccountRepository bankAccountRepository;
    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;
    private final UserService userService;
    private final WalletMapper walletMapper;

    @Override
    @Transactional
    @PreAuthorize("hasRole('USER')")
    public WalletResponse registerNewWallet() {
        var userId = userService.getProfile().getId();
        var user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        var newWallet = Wallet.builder().user(user).balance(0.0).build();

        return walletMapper.walletToWalletResponse(walletRepository.save(newWallet));
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('USER')")
    public void charge(String walletId, double amount) {
        try {
            var userId = userService.getProfile().getId();
            var user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

            var wallet =
                    walletRepository.findById(walletId).orElseThrow(() -> new AppException(ErrorCode.WALLET_NOT_FOUND));

            var currentBalance = user.getWallet().getBalance();
            double newBalance = currentBalance + amount;
            wallet.setBalance(newBalance);
        } catch (RuntimeException e) {
            throw new RuntimeException("some thing wrong when try to charge into wallet", e);
        }
    }

    @Override
    @PreAuthorize("hasRole('USER')")
    public Double showBalance(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        return user.getWallet().getBalance();
    }

    @Override
    @PreAuthorize("hasRole('USER')")
    public void fundTransfer(String fromUserId, String toUserId, Double amount) {}

    // Nap tien
    @Override
    @PreAuthorize("hasRole('USER')")
    @Transactional
    public void depositMoney(String userId, Double amount) {
        User depositUser;
        depositUser = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        var wallet = depositUser.getWallet();
        BankAccount bankAccount = bankAccountRepository.findByWalletId(wallet.getWalletId());
        if (bankAccount == null) {
            throw new AppException(ErrorCode.BANK_ACCOUNT_NOT_EXISTED);
        }

        if (bankAccount.getBankBalance() == 0 || bankAccount.getBankBalance() < amount) {
            throw new AppException(ErrorCode.INSUFFICIENT_BALANCE);
        }

        try {
            wallet.setBalance(wallet.getBalance() + amount);
            bankAccount.setBankBalance(bankAccount.getBankBalance() - amount);
            bankAccountRepository.save(bankAccount);
            walletRepository.save(wallet);
        } catch (Exception e) {
            throw new AppException(ErrorCode.DEPOSIT_FAIL);
        }

        // Transaction detail
        Transaction transaction = new Transaction();
        transaction.setTransactionType(TransactionType.DEPOSIT_TO_WALLET);
        transaction.setCreateDate(LocalDateTime.now());
        transaction.setAmount(amount);
        transaction.setWallet(wallet);
        transaction.setUser(depositUser);
        wallet.getTransaction().add(transaction);
        transactionRepository.save(transaction);
    }

    // Rut tien vao bank
    @Override
    @PreAuthorize("hasRole('USER')")
    public void withdrawMoney(String userId, Double amount) {
        User withdrawUser =
                userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        Wallet wallet = withdrawUser.getWallet();
        BankAccount bankAccount = bankAccountRepository.findByWalletId(wallet.getWalletId());
        if (bankAccount == null) {
            throw new AppException(ErrorCode.BANK_ACCOUNT_NOT_EXISTED);
        }
        if (wallet.getBalance() < amount) {
            throw new AppException(ErrorCode.INSUFFICIENT_BALANCE);
        }

        try {
            wallet.setBalance(wallet.getBalance() - amount);
            bankAccount.setBankBalance(bankAccount.getBankBalance() + amount);
            bankAccountRepository.save(bankAccount);
            walletRepository.save(wallet);
        } catch (Exception e) {
            throw new AppException(ErrorCode.WITHDRAW_FAIL);
        }

        // Transaction detail
        Transaction transaction = new Transaction();
        transaction.setTransactionType(TransactionType.WITHDRAW_TO_BANK);
        transaction.setCreateDate(LocalDateTime.now());
        transaction.setAmount(amount);
        transaction.setWallet(wallet);
        transaction.setUser(withdrawUser);
        wallet.getTransaction().add(transaction);
        transactionRepository.save(transaction);
    }
}
