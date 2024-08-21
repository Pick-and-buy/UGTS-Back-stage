package com.ugts.bankaccount.repository;

import com.ugts.bankaccount.entity.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankAccountRepository extends JpaRepository<BankAccount, String> {
    BankAccount findByWalletId(String walletId);

    BankAccount findByBankBin(String bankBin);
}
