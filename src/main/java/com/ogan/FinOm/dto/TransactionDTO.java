package com.ogan.FinOm.dto;

import com.ogan.FinOm.enums.TransactionType;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Hidden
public class TransactionDTO {
    private TransactionType transactionType;
    private BigDecimal amount;
    private String accountNumber;
    private String status;
}
