package com.ugts.transaction.service.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Random;

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
import com.ugts.vnpay.configuration.VnPayConfiguration;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public TransactionResponse createTransaction(TransactionRequest transactionRequest, String orderId) {
        var order = orderRepository.findById(orderId).orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

        // get user from context holder
        var contextHolder = SecurityContextHolder.getContext();
        String phoneNumber = contextHolder.getAuthentication().getName();

        var buyer = userRepository
                .findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        String id = VnPayConfiguration.getRandomNumber(8);

        var transaction = Transaction.builder()
                .billNo(getRandomBillNumber())
                .transNo(id)
                .bankCode(transactionRequest.getBankCode())
                .cardType(transactionRequest.getCardType())
                .amount((int) order.getPost().getProduct().getPrice())
                .currency(transactionRequest.getCurrency())
                .bankAccountNo(transactionRequest.getBankAccountNo())
                .bankAccount(transactionRequest.getBankAccount())
                .refundBankCode(transactionRequest.getRefundBankCode())
                .reason(transactionRequest.getReason())
                .createDate(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")))
                .transactionStatus(TransactionStatus.SUCCESS)
                .user(buyer)
                .order(order)
                .build();

        if(transactionRepository.findByTransNo(id) != null){
            id = VnPayConfiguration.getRandomNumber(8);
        }

        transaction.setTransNo(id);

        // TODO: implement notification

        return transactionMapper.toTransactionResponse(transactionRepository.save(transaction));
    }

    private String getRandomBillNumber() {
        Random random = new Random();
        StringBuilder billNo = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            int digit = random.nextInt(10);
            billNo.append(digit);
        }
        return billNo.toString();
    }
}
