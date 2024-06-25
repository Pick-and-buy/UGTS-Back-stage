package com.ugts.transaction.mapper;

import com.ugts.transaction.dto.TransactionResponse;
import com.ugts.transaction.entity.Transaction;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
    TransactionResponse toTransactionResponse(Transaction transaction);
}
