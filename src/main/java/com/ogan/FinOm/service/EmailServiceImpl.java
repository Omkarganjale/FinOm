package com.ogan.FinOm.service;

import com.ogan.FinOm.dto.EmailDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String senderEmail;

    /**
     * Sends email with given subject, recipient, messageBody
     */
    @Override
    public void sendEmail(EmailDetails emailDetails) {
        try{
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setFrom(senderEmail);
            mail.setTo(emailDetails.getRecipient());
            mail.setSubject(emailDetails.getSubject());
            mail.setText(emailDetails.getMessageBody());

            javaMailSender.send(mail);
            System.out.println("Mail Sent Successfully!"); //TODO: logger
        } catch (MailException e) {
            throw new RuntimeException(e);
        }
    }
}
