package com.ugts.wallet.repository;

import java.util.Optional;

import com.ugts.user.entity.User;
import com.ugts.wallet.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepository extends JpaRepository<Wallet, String> {
    Optional<Wallet> findByUser(User user);
}
