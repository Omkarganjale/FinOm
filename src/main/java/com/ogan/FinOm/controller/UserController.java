package com.ogan.FinOm.controller;

import com.ogan.FinOm.dto.BankResponse;
import com.ogan.FinOm.dto.requests.EnquiryRequest;
import com.ogan.FinOm.dto.requests.TransactionRequest;
import com.ogan.FinOm.dto.requests.TransferRequest;
import com.ogan.FinOm.dto.requests.UserRequest;
import com.ogan.FinOm.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@Tag(name = "Bank Account Holder Service APIs")
public class UserController {

    @Autowired
    UserService userService;

    @Operation(
            summary = "Create New Account",
            description = "Creates New Account and assigns an account number"
    )
    @ApiResponse(
            responseCode = "201",
            description = "Http Status 201 CREATED"
    )
    @PostMapping(path="/add")
    public BankResponse createNewAccount(@RequestBody UserRequest userRequest){
        return userService.createUser(userRequest);
    }

    @Operation(
            summary = "Account Balance Enquiry",
            description = "Given an account number, returns current Account Balance"
    )
    @ApiResponse(
            responseCode = "203",
            description = "Http Status 203 SUCCESS"
    )
    @GetMapping(path="/enquire/accountBalance")
    public BankResponse enquireAccountBalance(@RequestBody EnquiryRequest enquiryRequest){
        return userService.enquireBalance(enquiryRequest);
    }

    @Operation(
            summary = "Account Name Enquiry",
            description = "Given an account number, returns the account holder's name"
    )
    @ApiResponse(
            responseCode = "203",
            description = "Http Status 203 SUCCESS"
    )
    @GetMapping(path="/enquire/accountName")
    public String enquireAccountName(@RequestBody EnquiryRequest enquiryRequest){
        return userService.enquireName(enquiryRequest);
    }

    @Operation(
            summary = "Credit Transaction",
            description = "Credits a specified amount to the account"
    )
    @ApiResponse(
            responseCode = "202",
            description = "Http Status 202 SUCCESS"
    )
    @PostMapping(path="/transaction/credit")
    public BankResponse creditTransaction(@RequestBody TransactionRequest transactionRequest){
        return userService.creditAccount(transactionRequest);
    }

    @Operation(
            summary = "Debit Transaction",
            description = "Debits a specified amount from the account"
    )
    @ApiResponse(
            responseCode = "202",
            description = "Http Status 202 SUCCESS"
    )
    @PostMapping(path="/transaction/debit")
    public BankResponse debitTransaction(@RequestBody TransactionRequest transactionRequest){
        return userService.debitAccount(transactionRequest);
    }

    @Operation(
            summary = "Transfer Funds",
            description = "Transfers funds from one account to another"
    )
    @ApiResponse(
            responseCode = "202",
            description = "Http Status 202 SUCCESS"
    )
    @PostMapping(path="/transaction/transfer")
    public BankResponse transfer(@RequestBody TransferRequest transferRequest){
        return userService.transfer(transferRequest);
    }
}
