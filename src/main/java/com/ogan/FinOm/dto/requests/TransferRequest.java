package com.ogan.FinOm.dto.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransferRequest {
    @Schema(
            name = "Sender's Account Number"
    )
    private String senderAccountNumber;

    @Schema(
            name = "Recipient's Account Number"
    )
    private String recipientAccountNumber;

    @Schema(
            name = "Transaction Amount"
    )
    private BigDecimal amount; //TODO: round off upto 2 decimal places
}
