package com.ugts.transaction.service.impl;

import java.util.List;

import com.ugts.exception.AppException;
import com.ugts.exception.ErrorCode;
import com.ugts.transaction.dto.TransactionResponse;
import com.ugts.transaction.mapper.TransactionMapper;
import com.ugts.transaction.repository.TransactionRepository;
import com.ugts.transaction.service.TransactionService;
import com.ugts.user.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class TransactionServiceImpl implements TransactionService {
    UserRepository userRepository;

    TransactionRepository transactionRepository;

    TransactionMapper transactionMapper;

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public List<TransactionResponse> getAllTransactions() {
        var transactions = transactionRepository.findAll(Sort.by(Sort.Direction.DESC, "createDate"));
        return transactionMapper.toTransactionsResponse(transactions);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public List<TransactionResponse> getTransactionsByUserId(String userId) {
        var user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        return transactionMapper.toTransactionsResponse(user.getWallet().getTransactions());
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public TransactionResponse getTransactionById(String transactionId) {
        var transaction = transactionRepository
                .findById(transactionId)
                .orElseThrow(() -> new AppException(ErrorCode.TRANSACTION_NOT_EXISTED));
        return transactionMapper.toTransactionResponse(transaction);
    }
}
