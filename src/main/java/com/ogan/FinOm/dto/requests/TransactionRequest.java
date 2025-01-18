package com.ogan.FinOm.dto.requests;

import com.ogan.FinOm.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRequest {
    private String accountNumber;
    private TransactionType transactionType;
    private BigDecimal amount; //TODO: round off upto 2 decimal places
}
