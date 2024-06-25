package com.ugts.transaction.service.impl;

import com.ugts.exception.AppException;
import com.ugts.exception.ErrorCode;
import com.ugts.order.repository.OrderRepository;
import com.ugts.transaction.dto.TransactionRequest;
import com.ugts.transaction.dto.TransactionResponse;
import com.ugts.transaction.entity.Transaction;
import com.ugts.transaction.enums.TransactionStatus;
import com.ugts.transaction.mapper.TransactionMapper;
import com.ugts.transaction.repository.TransactionRepository;
import com.ugts.transaction.service.TransactionService;
import com.ugts.user.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class TransactionServiceImpl implements TransactionService {

    OrderRepository orderRepository;

    UserRepository userRepository;

    TransactionRepository transactionRepository;

    TransactionMapper transactionMapper;

    @Override
    @Transactional
    @PreAuthorize("hasAnyRole('USER')")
    public TransactionResponse createTransaction(TransactionRequest transactionRequest) {
        var order = orderRepository.findById(transactionRequest.getOrder().getId())
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

        // get user from context holder
        var contextHolder = SecurityContextHolder.getContext();
        String phoneNumber = contextHolder.getAuthentication().getName();

        var user = userRepository
                .findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        var transaction = Transaction.builder()
                .bankCode(transactionRequest.getBankCode())
                .cardType(transactionRequest.getCardType())
                .amount(transactionRequest.getAmount())
                .currency(transactionRequest.getCurrency())
                .bankAccountNo(transactionRequest.getBankAccountNo())
                .bankAccount(transactionRequest.getBankAccount())
                .refundBankCode(transactionRequest.getRefundBankCode())
                .reason(transactionRequest.getReason())
                .createDate(new Date())
                .transactionStatus(TransactionStatus.SUCCESS)
                .user(user)
                .order(order)
                .build();

        return transactionMapper.toTransactionResponse(transactionRepository.save(transaction));
    }
}
