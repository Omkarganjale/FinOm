package com.ogan.FinOm.service;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.ogan.FinOm.dto.EmailDetails;
import com.ogan.FinOm.entity.Transaction;
import com.ogan.FinOm.entity.User;
import com.ogan.FinOm.repository.TransactionRepository;
import com.ogan.FinOm.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.ogan.FinOm.utils.AccountUtils.getAccountName;

@Service
@Slf4j
public class BankStatementService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Value("${finom.statement.path}")
    private String statementsFolder;

    /**
     * Generates Bank Statement for given account Number from startDate to endDate
     */
    public List<Transaction> generateStatement(String accountNumber, String startDate, String endDate) throws DocumentException, FileNotFoundException {
        LocalDate localStartDate = LocalDate.parse(startDate, DateTimeFormatter.ISO_DATE);
        LocalDate localEndDate = LocalDate.parse(endDate, DateTimeFormatter.ISO_DATE);
        List<Transaction> txList = transactionRepository.findAll().stream()
                .filter(tx -> tx.getAccountNumber().equals(accountNumber))
                .filter(tx -> tx.getCreatedAt().isAfter(localStartDate.atStartOfDay()))
                .filter(tx -> tx.getCreatedAt().isBefore(localEndDate.plusDays(1).atStartOfDay()))
                .toList();

        User user = userRepository.findByAccountNumber(accountNumber);


        String filePath = designStatement(user, startDate, endDate, txList);

        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(user.getEmail())
                .subject(String.format("FinOm Account Statement for Period %s to %s", startDate, endDate))
                .messageBody("Please find your requested account statement attached!")
                .attachment(filePath)
                .build();

        emailService.sendEmailWithAttachment(emailDetails);

        return txList;
    }

    public String designStatement(User user,
                                String startDate,
                                String endDate,
                                List<Transaction> transactionList) throws FileNotFoundException, DocumentException {

        String statementsPath = statementsFolder + "Statement_" + user.getAccountNumber() + ".pdf";
        Rectangle statementPageSize = new Rectangle(PageSize.A4);
        Document document = new Document(statementPageSize, 36, 36, 50, 50); // Added margins
        OutputStream outputStream = new FileOutputStream(statementsPath);
        PdfWriter writer = PdfWriter.getInstance(document, outputStream);
        document.open();

        // Bank Header Section
        PdfPTable bankInfoTable = new PdfPTable(2);
        bankInfoTable.setWidthPercentage(100);
        bankInfoTable.setSpacingAfter(20);

        PdfPCell bankLogo = new PdfPCell(new Phrase("FinOm")); // Placeholder for the bank logo
        bankLogo.setBorder(0);
        bankLogo.setPadding(10f);
        bankLogo.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);

        PdfPCell bankDetails = new PdfPCell(new Phrase("Yet Another Registered Bank\nThe FinOm Bank,\nPune Maharashtra"));
        bankDetails.setBorder(0);
        bankDetails.setBackgroundColor(BaseColor.LIGHT_GRAY);
        bankDetails.setPadding(10f);
        bankDetails.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);

        bankInfoTable.addCell(bankLogo);
        bankInfoTable.addCell(bankDetails);

        document.add(bankInfoTable);

        // Statement Info Section
        PdfPTable statementInfoTable = new PdfPTable(2);
        statementInfoTable.setWidthPercentage(100);
        statementInfoTable.setSpacingAfter(20);

        PdfPCell statementHeader = new PdfPCell(new Phrase("STATEMENT OF ACCOUNT", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14)));
        statementHeader.setBorder(0);
        statementHeader.setColspan(2);
        statementHeader.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        statementHeader.setPadding(10f);

        statementInfoTable.addCell(statementHeader);
        statementInfoTable.addCell(createCell("Start Date: " + startDate, PdfPCell.ALIGN_LEFT));
        statementInfoTable.addCell(createCell("End Date: " + endDate, PdfPCell.ALIGN_RIGHT));
        statementInfoTable.addCell(createCell("Customer Name: " + getAccountName(user), PdfPCell.ALIGN_LEFT));
        statementInfoTable.addCell(createCell("Account Number: " + user.getAccountNumber(), PdfPCell.ALIGN_RIGHT));
        statementInfoTable.addCell(createCell("Customer Address: " + user.getAddress(), PdfPCell.ALIGN_LEFT));

        document.add(statementInfoTable);

        // Transactions Table
        PdfPTable transactionTable = new PdfPTable(4);
        transactionTable.setWidthPercentage(100);
        transactionTable.setSpacingBefore(10);
        transactionTable.setSpacingAfter(20);
        transactionTable.setWidths(new int[]{3, 3, 3, 3});

        // Header Row
        transactionTable.addCell(createHeaderCell("DATE"));
        transactionTable.addCell(createHeaderCell("TRANSACTION TYPE"));
        transactionTable.addCell(createHeaderCell("AMOUNT"));
        transactionTable.addCell(createHeaderCell("STATUS"));

        // Transaction Data Rows
        transactionList.forEach(transaction -> {
            transactionTable.addCell(createCell(transaction.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")), PdfPCell.ALIGN_LEFT));
            transactionTable.addCell(createCell(transaction.getTransactionType().toString(), PdfPCell.ALIGN_LEFT));
            transactionTable.addCell(createCell(transaction.getAmount().toString(), PdfPCell.ALIGN_RIGHT));
            transactionTable.addCell(createCell(transaction.getStatus(), PdfPCell.ALIGN_CENTER));
        });

        document.add(transactionTable);

        // Footer Section
        PdfPTable footerTable = new PdfPTable(1);
        footerTable.setWidthPercentage(100);

        PdfPCell footerCell = new PdfPCell(new Phrase("This document is confidential and intended for authorized use only."));
        footerCell.setBorder(0);
        footerCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        footerCell.setPadding(10f);
        footerTable.addCell(footerCell);

        document.add(footerTable);

        document.close();

        return statementsPath;
    }

    // Helper methods for cleaner cell creation
    private PdfPCell createHeaderCell(String content) {
        PdfPCell cell = new PdfPCell(new Phrase(content, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE)));
        cell.setBackgroundColor(BaseColor.BLUE);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setPadding(10f);
        return cell;
    }

    private PdfPCell createCell(String content, int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(content, FontFactory.getFont(FontFactory.HELVETICA, 10)));
        cell.setHorizontalAlignment(alignment);
        cell.setPadding(8f);
        cell.setBorderWidth(0.5f);
        return cell;
    }
}
