package com.ogan.FinOm.utils;

import com.ogan.FinOm.dto.requests.TransactionRequest;
import com.ogan.FinOm.enums.TransactionType;

import java.math.BigDecimal;
import java.util.function.Predicate;

public class Predicates {
    public static final Predicate<TransactionRequest> isCreditTransaction = txReq -> txReq.getTransactionType().equals(TransactionType.CREDIT);

    public static final Predicate<TransactionRequest> isDebitTransaction = txReq -> txReq.getTransactionType().equals(TransactionType.DEBIT);

    // Positive Value > 0
    public static final Predicate<TransactionRequest> isNonZeroTransaction = txReq -> txReq.getAmount().compareTo(BigDecimal.ZERO)>0;

    public static final Predicate<TransactionRequest> isNonZeroCreditTransaction = txReq -> isCreditTransaction.and(isNonZeroTransaction).test(txReq);

    public static final Predicate<TransactionRequest> isNonZeroDebitTransaction = txReq -> isDebitTransaction.and(isNonZeroTransaction).test(txReq);
}
