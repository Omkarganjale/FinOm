package com.ogan.FinOm.service;

import com.ogan.FinOm.dto.BankResponse;
import com.ogan.FinOm.dto.requests.EnquireRequest;
import com.ogan.FinOm.dto.requests.TransactionRequest;
import com.ogan.FinOm.dto.requests.UserRequest;

public interface UserService {
    BankResponse createUser(UserRequest userRequest);

    BankResponse enquireBalance(EnquireRequest enquireRequest);

    String enquireName(EnquireRequest enquireRequest);

    BankResponse creditAccount(TransactionRequest transactionRequest);

    BankResponse debitAccount(TransactionRequest transactionRequest);
}
