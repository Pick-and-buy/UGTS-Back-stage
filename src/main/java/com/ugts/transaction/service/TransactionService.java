package com.ugts.transaction.service;

import com.ugts.transaction.dto.TransactionRequest;
import com.ugts.transaction.dto.TransactionResponse;
import com.ugts.transaction.entity.Transaction;

public interface TransactionService {
    TransactionResponse createTransaction(TransactionRequest transactionRequest);
}
