package com.ogan.FinOm.service;

import com.ogan.FinOm.dto.AccountInfo;
import com.ogan.FinOm.dto.BankResponse;
import com.ogan.FinOm.dto.EmailDetails;
import com.ogan.FinOm.dto.TransactionDTO;
import com.ogan.FinOm.dto.requests.EnquiryRequest;
import com.ogan.FinOm.dto.requests.LoginRequest;
import com.ogan.FinOm.dto.requests.TransactionRequest;
import com.ogan.FinOm.dto.requests.TransferRequest;
import com.ogan.FinOm.dto.requests.UserRequest;
import com.ogan.FinOm.entity.User;
import com.ogan.FinOm.enums.TransactionType;
import com.ogan.FinOm.mapper.UserMapper;
import com.ogan.FinOm.repository.UserRepository;
import com.ogan.FinOm.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.function.Predicate;

import static com.ogan.FinOm.utils.AccountUtils.getAccountName;
import static com.ogan.FinOm.utils.Constants.*;
import static com.ogan.FinOm.utils.EmailUtils.getNewAccountCreatedEmailAlert;
import static com.ogan.FinOm.utils.EmailUtils.getTransactionSuccessEmailAlert;
import static com.ogan.FinOm.utils.Predicates.*;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    UserRepository userRepository;

    @Autowired
    EmailService emailService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    TransactionService transactionService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

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
        userRequest.setPassword(passwordEncoder.encode(userRequest.getPassword()));
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
    public BankResponse enquireBalance(EnquiryRequest enquiryRequest) {

        boolean accountExists = userRepository.existsByAccountNumber(enquiryRequest.getAccountNumber());
        if(!accountExists){
            return BankResponse.builder()
                    .responseCode(ACCOUNT_UNAVAILABLE_CODE)
                    .responseMessage(ACCOUNT_UNAVAILABLE_MSG)
                    .build();
        }

        User user = userRepository.findByAccountNumber(enquiryRequest.getAccountNumber());

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
    public String enquireName(EnquiryRequest enquiryRequest) {
        boolean accountExists = userRepository.existsByAccountNumber(enquiryRequest.getAccountNumber());
        if(!accountExists){
            return ACCOUNT_UNAVAILABLE_MSG;
        }

        User user = userRepository.findByAccountNumber(enquiryRequest.getAccountNumber());

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
        transactionService.saveTransactionRequest(transactionRequest);
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

    /**
     * Debits an Account for Withdrawal Transactions
     */
    @Override
    public BankResponse debitAccount(TransactionRequest transactionRequest){
        boolean accountExists = userRepository.existsByAccountNumber(transactionRequest.getAccountNumber());
        if(!accountExists){
            return BankResponse.builder()
                    .responseCode(ACCOUNT_UNAVAILABLE_CODE)
                    .responseMessage(ACCOUNT_UNAVAILABLE_MSG)
                    .build();
        }

        boolean validations = validateTransactionRequest(transactionRequest, isNonZeroTransaction, isDebitTransaction);
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
        transactionService.saveTransactionRequest(transactionRequest);
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

    /**
     * Transfers Balance from one account to another
     */
    @Override
    public BankResponse transfer(TransferRequest transferRequest) {

        if(!userRepository.existsByAccountNumber(transferRequest.getSenderAccountNumber())){
            return BankResponse.builder()
                    .responseCode(ACCOUNT_UNAVAILABLE_CODE)
                    .responseMessage("Sender ".concat(ACCOUNT_UNAVAILABLE_MSG))
                    .build();
        }

        String user = SecurityContextHolder.getContext().getAuthentication().getName();

        User debitUser = userRepository.findByAccountNumber(transferRequest.getSenderAccountNumber());

        if(!debitUser.getEmail().equalsIgnoreCase(user)){
            return BankResponse.builder()
                    .responseCode(UNAUTHORIZED_TRANSACTION_CODE)
                    .responseMessage(UNAUTHORIZED_TRANSACTION_MSG)
                    .build();
        }

        if(debitUser.getAccountBalance().compareTo(transferRequest.getAmount())<0){
            return BankResponse.builder()
                    .responseCode(INSUFFICIENT_ACC_BALANCE_CODE)
                    .responseMessage(INSUFFICIENT_ACC_BALANCE_MSG)
                    .build();
        }

        if(!userRepository.existsByAccountNumber(transferRequest.getRecipientAccountNumber())){
            return BankResponse.builder()
                    .responseCode(ACCOUNT_UNAVAILABLE_CODE)
                    .responseMessage("Recipient ".concat(ACCOUNT_UNAVAILABLE_MSG))
                    .build();
        }

        if(transferRequest.getAmount().compareTo(BigDecimal.ZERO)<=0 || transferRequest.getSenderAccountNumber().equals(transferRequest.getRecipientAccountNumber())){
            return BankResponse.builder()
                    .responseCode(TRANSACTION_VALIDATION_FAILED_CODE)
                    .responseMessage(TRANSACTION_VALIDATION_FAILED_MSG)
                    .build();
        }

        User creditUser = userRepository.findByAccountNumber(transferRequest.getRecipientAccountNumber());

        debitUser.setAccountBalance(debitUser.getAccountBalance().subtract(transferRequest.getAmount()));
        creditUser.setAccountBalance(creditUser.getAccountBalance().add(transferRequest.getAmount()));

        debitUser = userRepository.save(debitUser);
        creditUser = userRepository.save(creditUser);

        emailService.sendEmail(getTransactionSuccessEmailAlert(debitUser, TransactionType.DEBIT, transferRequest.getAmount()));
        emailService.sendEmail(getTransactionSuccessEmailAlert(creditUser, TransactionType.CREDIT, transferRequest.getAmount()));

        transactionService.saveTransaction(TransactionDTO.builder()
                .accountNumber(debitUser.getAccountNumber())
                .transactionType(TransactionType.DEBIT)
                .amount(transferRequest.getAmount())
                .build());
        transactionService.saveTransaction(TransactionDTO.builder()
                .accountNumber(creditUser.getAccountNumber())
                .transactionType(TransactionType.CREDIT)
                .amount(transferRequest.getAmount())
                .build());

        return BankResponse.builder()
                .responseCode(TRANSACTION_SUCCESS_CODE)
                .responseMessage(TRANSACTION_SUCCESS_MSG)
                .build();
    }

    @SafeVarargs
    private static <TransactionRequest> boolean validateTransactionRequest(TransactionRequest testObject, Predicate<TransactionRequest>... predicates) {
        for (Predicate<TransactionRequest> predicate : predicates) {
            if (!predicate.test(testObject)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public BankResponse login(LoginRequest loginRequest){
        Authentication authentication = authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );

        EmailDetails loginAlert = EmailDetails.builder()
                .subject("New Login Detected")
                .recipient(loginRequest.getEmail())
                .messageBody("New login is detected. \nIf you did not initiate please contact bank immediately")
                .build();

        emailService.sendEmail(loginAlert);

        return BankResponse.builder()
                .responseCode(LOGIN_SUCCESS_CODE)
                .responseMessage(jwtTokenProvider.generateToken(authentication))
                .build();
    }
}
