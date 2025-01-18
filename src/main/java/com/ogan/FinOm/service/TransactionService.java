package com.ogan.FinOm.service;

import com.ogan.FinOm.dto.TransactionDTO;
import com.ogan.FinOm.dto.requests.TransactionRequest;

public interface TransactionService {
    void saveTransaction(TransactionDTO transaction);

    public void saveTransactionRequest(TransactionRequest transactionRequest);
}
