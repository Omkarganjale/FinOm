package com.ogan.FinOm.utils;

import com.ogan.FinOm.dto.EmailDetails;
import com.ogan.FinOm.entity.User;
import com.ogan.FinOm.enums.TransactionType;

import java.math.BigDecimal;
import java.text.MessageFormat;

import static com.ogan.FinOm.utils.AccountUtils.getAccountName;

public class EmailUtils {

    private static final String newAccBodyTemplate = """
                                Congratulations! Your New Bank Account Request is Complete.
                                Your Account Details:
                                Account Number: {0}
                                Account Name: {1}
                                Account Balance: {2}
                                Account Status: {3}
                                """;

    private static final String transactionSuccessBodyTemplate = """
                                Transaction Successful!
                                Your Account Details:
                                Transaction Type: {0}
                                Account Number: {1}
                                Transaction Amount: {2}
                                Current Account Balance: {3}
                                """;

    public static EmailDetails getNewAccountCreatedEmailAlert(User savedUser){
        return EmailDetails.builder()
                .recipient(savedUser.getEmail())
                .subject("Account Created")
                .messageBody(MessageFormat.format(newAccBodyTemplate,
                        savedUser.getAccountNumber(),
                        getAccountName(savedUser),
                        savedUser.getAccountBalance(),
                        savedUser.getStatus())
                ).build();
    }

    public static EmailDetails getTransactionSuccessEmailAlert(User user, TransactionType transactionType, BigDecimal txAmount){
        return EmailDetails.builder()
                .recipient(user.getEmail())
                .subject(String.format("%s Transaction Alert", transactionType))
                .messageBody(MessageFormat.format(transactionSuccessBodyTemplate,
                        transactionType,
                        user.getAccountNumber(),
                        txAmount,
                        user.getAccountBalance())
                ).build();
    }
}
