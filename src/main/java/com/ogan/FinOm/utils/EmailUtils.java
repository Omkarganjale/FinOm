package com.ogan.FinOm.utils;

import com.ogan.FinOm.dto.EmailDetails;
import com.ogan.FinOm.entity.User;

import java.text.MessageFormat;

import static com.ogan.FinOm.utils.AccountUtils.getAccountName;

public class EmailUtils {

    private static final String newAccBodyTemplate = """
                                Congratulations! Your account has been successfully created.
                                Your Account Details:
                                Account Number: {0}
                                Account Name: {1}
                                Account Balance: {2}
                                Account Status: {3}
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
}
