package com.ogan.FinOm.service;

import com.ogan.FinOm.dto.*;
import com.ogan.FinOm.dto.requests.EnquireRequest;
import com.ogan.FinOm.dto.requests.TransactionRequest;
import com.ogan.FinOm.dto.requests.UserRequest;
import com.ogan.FinOm.entity.User;
import com.ogan.FinOm.mapper.UserMapper;
import com.ogan.FinOm.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.function.Predicate;

import static com.ogan.FinOm.utils.AccountUtils.getAccountName;
import static com.ogan.FinOm.utils.Constants.*;
import static com.ogan.FinOm.utils.EmailUtils.getNewAccountCreatedEmailAlert;
import static com.ogan.FinOm.utils.Predicates.*;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    UserRepository userRepository;

    @Autowired
    EmailService emailService;

    /**
     * Creates a new User
     * Check if user already exists
     * Sends an email to newly created User
     */
    @Override
    public BankResponse createUser(UserRequest userRequest) {

        if(userRepository.existsByEmail(userRequest.getEmail())){
            return BankResponse.builder()
                    .responseCode(ACCOUNT_EXISTS_CODE)
                    .responseMessage(ACCOUNT_EXISTS_MSG)
                    .build();
        }

        User user = userMapper.toEntity(userRequest);

        User savedUser = userRepository.save(user);

        emailService.sendEmail(getNewAccountCreatedEmailAlert(savedUser));

        return BankResponse.builder()
                .responseCode(ACCOUNT_CREATED_CODE)
                .responseMessage(ACCOUNT_CREATED_MSG)
                .accountInfo(AccountInfo.builder()
                        .accountNumber(savedUser.getAccountNumber())
                        .accountName(getAccountName(savedUser))
                        .accountBalance(savedUser.getAccountBalance())
                        .status(savedUser.getStatus())
                        .build())
                .build();

    }

    /**
     * Account Balance Enquiry for given accountNumber
     */
    @Override
    public BankResponse enquireBalance(EnquireRequest enquireRequest) {

        boolean accountExists = userRepository.existsByAccountNumber(enquireRequest.getAccountNumber());
        if(!accountExists){
            return BankResponse.builder()
                    .responseCode(ACCOUNT_UNAVAILABLE_CODE)
                    .responseMessage(ACCOUNT_UNAVAILABLE_MSG)
                    .build();
        }

        User user = userRepository.findByAccountNumber(enquireRequest.getAccountNumber());

        return BankResponse.builder()
                .responseCode(ENQUIRY_SUCCESS_CODE)
                .responseMessage(ENQUIRY_SUCCESS_MSG)
                .accountInfo(AccountInfo.builder()
                        .accountName(getAccountName(user))
                        .accountNumber(user.getAccountNumber())
                        .accountBalance(user.getAccountBalance())
                        .status(user.getStatus())
                        .build()
                ).build();
    }

    /**
     * Account Name Enquiry for given accountNumber
     */
    @Override
    public String enquireName(EnquireRequest enquireRequest) {
        boolean accountExists = userRepository.existsByAccountNumber(enquireRequest.getAccountNumber());
        if(!accountExists){
            return ACCOUNT_UNAVAILABLE_MSG;
        }

        User user = userRepository.findByAccountNumber(enquireRequest.getAccountNumber());

        return getAccountName(user);
    }

    /**
     * Credits an Account for Deposit Transactions
     */
    @Override
    public BankResponse creditAccount(TransactionRequest transactionRequest){
        boolean accountExists = userRepository.existsByAccountNumber(transactionRequest.getAccountNumber());
        if(!accountExists){
            return BankResponse.builder()
                    .responseCode(ACCOUNT_UNAVAILABLE_CODE)
                    .responseMessage(ACCOUNT_UNAVAILABLE_MSG)
                    .build();
        }

        boolean validations = validateTransactionRequest(transactionRequest, isNonZeroTransaction, isCreditTransaction);
        if(!validations){
            return BankResponse.builder()
                    .responseCode(TRANSACTION_VALIDATION_FAILED_CODE)
                    .responseMessage(TRANSACTION_VALIDATION_FAILED_MSG)
                    .build();
        }

        User user = userRepository.findByAccountNumber(transactionRequest.getAccountNumber());
        user.setAccountBalance(user.getAccountBalance().add(transactionRequest.getAmount()));
        User savedUser = userRepository.save(user);
        return BankResponse.builder()
                .responseCode(TRANSACTION_SUCCESS_CODE)
                .responseMessage(TRANSACTION_SUCCESS_MSG)
                .accountInfo(AccountInfo.builder()
                        .accountNumber(savedUser.getAccountNumber())
                        .accountName(getAccountName(savedUser))
                        .accountBalance(savedUser.getAccountBalance())
                        .status(savedUser.getStatus())
                        .build()
                ).build();
    }

    @Override
    public BankResponse debitAccount(TransactionRequest transactionRequest){
        boolean accountExists = userRepository.existsByAccountNumber(transactionRequest.getAccountNumber());
        if(!accountExists){
            return BankResponse.builder()
                    .responseCode(ACCOUNT_UNAVAILABLE_CODE)
                    .responseMessage(ACCOUNT_UNAVAILABLE_MSG)
                    .build();
        }

        boolean validations = validateTransactionRequest(transactionRequest, isNonZeroTransaction, isDebitTransaction); // TODO: handle errorcodes in try-catch
        if(!validations){
            return BankResponse.builder()
                    .responseCode(TRANSACTION_VALIDATION_FAILED_CODE)
                    .responseMessage(TRANSACTION_VALIDATION_FAILED_MSG)
                    .build();
        }

        User user = userRepository.findByAccountNumber(transactionRequest.getAccountNumber());

        if(user.getAccountBalance().compareTo(transactionRequest.getAmount())<0){
            return BankResponse.builder()
                    .responseCode(INSUFFICIENT_ACC_BALANCE_CODE)
                    .responseMessage(INSUFFICIENT_ACC_BALANCE_MSG)
                    .build();
        }

        user.setAccountBalance(user.getAccountBalance().subtract(transactionRequest.getAmount()));
        User savedUser = userRepository.save(user);
        return BankResponse.builder()
                .responseCode(TRANSACTION_SUCCESS_CODE)
                .responseMessage(TRANSACTION_SUCCESS_MSG)
                .accountInfo(AccountInfo.builder()
                        .accountNumber(savedUser.getAccountNumber())
                        .accountName(getAccountName(savedUser))
                        .accountBalance(savedUser.getAccountBalance())
                        .status(savedUser.getStatus())
                        .build()
                ).build();
    }

    //TODO:  transfer, userValidation (accountStatus=ACTIVE)

    @SafeVarargs
    private static <TransactionRequest> boolean validateTransactionRequest(TransactionRequest testObject, Predicate<TransactionRequest>... predicates) {
        for (Predicate<TransactionRequest> predicate : predicates) {
            if (!predicate.test(testObject)) {
                return false;
            }
        }
        return true;
    }
}
