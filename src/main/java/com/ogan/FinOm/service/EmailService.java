package com.ogan.FinOm.service;

import com.ogan.FinOm.dto.EmailDetails;

public interface EmailService {

    void sendEmail(EmailDetails emailDetails);

    void sendEmailWithAttachment(EmailDetails emailDetails);
}
