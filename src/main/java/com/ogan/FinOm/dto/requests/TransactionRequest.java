package com.ogan.FinOm.dto.requests;

import com.ogan.FinOm.enums.TransactionType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRequest {
    @Schema(
            name = "User Account Number"
    )
    private String accountNumber;

    @Schema(
            name = "Transaction Type [CREDIT/DEBIT]"
    )
    private TransactionType transactionType;

    @Schema(
            name = "Transaction Amount"
    )
    private BigDecimal amount;

    public void setAmount(BigDecimal amt){
        this.amount = amt.setScale(2, RoundingMode.HALF_EVEN);
    }
}
