package com.ugts.wallet.repository;

import com.ugts.user.entity.User;
import com.ugts.wallet.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WalletRepository extends JpaRepository<Wallet, String> {
    Optional<Wallet> findByUser(User user);
}
