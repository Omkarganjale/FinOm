package com.ogan.FinOm.utils;

import com.ogan.FinOm.entity.User;

import java.time.Year;

public class AccountUtils {

    /**
     * Generates Random AccountNumber
     * Account Number = Current Year + 6 digit random Number
     * e.g. 2025194273
     */
     public static String generateAccountNumber(){
         Year curr_year = Year.now();
         int min = 100000;
         int max = 999999;

         int randomNumber = (int) Math.floor((Math.random() * (max - min + 1) + min));
         String year = String.valueOf(curr_year);
         String randomNo = String.valueOf(randomNumber);

         return year.concat(randomNo);
     }

    /**
     * Get Account name
     */
    public static String getAccountName(User user){
        StringBuilder sb = new StringBuilder();
        sb.append(user.getFirstName()).append(" ").append(user.getLastName());
        return sb.toString();
    }
}
