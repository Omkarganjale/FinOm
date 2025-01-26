package com.ogan.FinOm.controller;

import com.itextpdf.text.DocumentException;
import com.ogan.FinOm.entity.Transaction;
import com.ogan.FinOm.service.BankStatementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/api/statement")
@Tag(name = "Statement APIs")
public class StatementController {

    @Autowired
    BankStatementService bankStatementService;

    @Operation(
            summary = "Bank Statement Enquiry",
            description = "Given Account Number, start date and end date, emails the pdf statement of transactions during that period"
    )
    @GetMapping(path="/statement")
    public List<Transaction> enquireAccountBalance(@RequestParam String accountNumber,
                                                   @RequestParam String startDate,
                                                   @RequestParam String endDate) throws DocumentException, FileNotFoundException {

        return bankStatementService.generateStatement(accountNumber, startDate, endDate);

    }

}
