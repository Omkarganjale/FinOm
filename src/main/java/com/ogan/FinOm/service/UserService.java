package com.ogan.FinOm.service;

import com.ogan.FinOm.dto.BankResponse;
import com.ogan.FinOm.dto.requests.EnquiryRequest;
import com.ogan.FinOm.dto.requests.TransactionRequest;
import com.ogan.FinOm.dto.requests.TransferRequest;
import com.ogan.FinOm.dto.requests.UserRequest;

public interface UserService {
    BankResponse createUser(UserRequest userRequest);

    BankResponse enquireBalance(EnquiryRequest enquiryRequest);

    String enquireName(EnquiryRequest enquiryRequest);

    BankResponse creditAccount(TransactionRequest transactionRequest);

    BankResponse debitAccount(TransactionRequest transactionRequest);

    BankResponse transfer(TransferRequest transferRequest);
}
