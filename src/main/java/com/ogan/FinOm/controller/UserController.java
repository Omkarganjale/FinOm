package com.ogan.FinOm.controller;

import com.ogan.FinOm.dto.BankResponse;
import com.ogan.FinOm.dto.requests.EnquireRequest;
import com.ogan.FinOm.dto.requests.TransactionRequest;
import com.ogan.FinOm.dto.requests.UserRequest;
import com.ogan.FinOm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping(path="/add")
    public BankResponse createNewAccount(@RequestBody UserRequest userRequest){
        return userService.createUser(userRequest);
    }

    @GetMapping(path="/enquire/accountBalance")
    public BankResponse enquireAccountBalance(@RequestBody EnquireRequest enquireRequest){
        return userService.enquireBalance(enquireRequest);
    }

    @GetMapping(path="/enquire/accountName")
    public String enquireAccountName(@RequestBody EnquireRequest enquireRequest){
        return userService.enquireName(enquireRequest);
    }

    @PostMapping(path="/transaction/credit")
    public BankResponse creditTransaction(@RequestBody TransactionRequest transactionRequest){
        return userService.creditAccount(transactionRequest);
    }

    @PostMapping(path="/transaction/debit")
    public BankResponse debitTransaction(@RequestBody TransactionRequest transactionRequest){
        return userService.debitAccount(transactionRequest);
    }
}
