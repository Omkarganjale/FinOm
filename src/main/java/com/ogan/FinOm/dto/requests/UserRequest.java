package com.ogan.FinOm.dto.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
    @Schema(
            name = "First Name of Account Holder"
    )
    private String firstName;

    @Schema(
            name = "Last Name of Account Holder"
    )
    private String lastName;

    @Schema(
            name = "Gender of Account Holder"
    )
    private String gender;

    @Schema(
            name = "Address of Account Holder"
    )
    private String address;

    @Schema(
            name = "Home State of Account Holder"
    )
    private String stateOfOrigin;

    @Schema(
            name = "Email Id of Account Holder"
    )
    private String email;

    @Schema(
            name = "Phone Number of Account Holder"
    )
    private String phoneNumber;
}
