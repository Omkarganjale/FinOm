package com.ogan.FinOm.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BankResponse {
    @Schema(
            name = "Bank Codes for Transaction Processing"
    )
    @NonNull
    private String responseCode;

    @Schema(
            name = "Bank Response for Transaction Processing"
    )
    @NonNull
    private String responseMessage;

    @Schema(
            name = "Details for Requested Account"
    )
    private AccountInfo accountInfo;
}
