package com.ugts.transaction.repository;

import com.ugts.transaction.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, String> {

    List<Transaction> findAllByUserId(String userId);

    Transaction findByTransNo(String transNo);
}
