package com.ugts.transaction.service;

import java.util.List;

import com.ugts.transaction.dto.TransactionResponse;

public interface TransactionService {
    List<TransactionResponse> getAllTransactions();

    List<TransactionResponse> getTransactionsByUserId(String userId);

    TransactionResponse getTransactionById(String postId);
}
