package com.ugts.transaction.repository;

import java.util.List;

import com.ugts.transaction.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, String> {

    List<Transaction> findAllByUserId(String userId);

    Transaction findByTransNo(String transNo);
}
