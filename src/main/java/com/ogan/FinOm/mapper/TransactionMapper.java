package com.ogan.FinOm.mapper;

import com.ogan.FinOm.dto.TransactionDTO;
import com.ogan.FinOm.dto.requests.TransactionRequest;
import com.ogan.FinOm.entity.Transaction;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
    Transaction toEntity(TransactionDTO transactionDTO);

    Transaction toEntity(TransactionRequest transactionRequest);

    @AfterMapping
    default void setEntityDetails(@MappingTarget Transaction transaction, TransactionDTO transactionDTO){
        transaction.setStatus("POSTED");
    }

    @AfterMapping
    default void setEntityDetails(@MappingTarget Transaction transaction, TransactionRequest transactionRequest){
        transaction.setStatus("POSTED");
    }
}
