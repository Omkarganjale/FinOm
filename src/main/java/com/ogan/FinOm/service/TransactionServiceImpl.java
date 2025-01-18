package com.ogan.FinOm.service;

import com.ogan.FinOm.dto.TransactionDTO;
import com.ogan.FinOm.dto.requests.TransactionRequest;
import com.ogan.FinOm.mapper.TransactionMapper;
import com.ogan.FinOm.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    TransactionMapper transactionMapper;

    @Override
    public void saveTransaction(TransactionDTO transactiondto) {
        transactionRepository.save(transactionMapper.toEntity(transactiondto));
    }

    @Override
    public void saveTransactionRequest(TransactionRequest transactionRequest) {
        transactionRepository.save(transactionMapper.toEntity(transactionRequest));
    }
}
