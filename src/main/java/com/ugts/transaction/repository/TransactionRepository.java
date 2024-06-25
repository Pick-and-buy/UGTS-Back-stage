package com.ugts.transaction.repository;

import com.ugts.transaction.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, String> {}
