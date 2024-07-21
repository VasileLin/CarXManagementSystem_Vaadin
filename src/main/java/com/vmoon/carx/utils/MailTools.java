package com.vmoon.carx.utils;

import com.vmoon.carx.services.CompanyService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@RequiredArgsConstructor
@Service
public class MailTools {
    private final JavaMailSender mailSender;
    private final CompanyService companyService;

    public void sendEmailWithAttachment(String to, byte[] pdfBytes) throws MessagingException, IOException {
        String companyName = companyService.getAllCompanies().get(0).getName();
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom("vmoon.apps@gmail.com");
        helper.setTo(to);
        helper.setSubject("Invoice for Your Recent Auto Service");
        helper.setText("Dear Customer,\nThank you for choosing our auto service.\nPlease find attached the invoice for the services rendered.\nIf you have any questions or require further assistance, feel free to contact us.\nBest regards,"+companyName);


        File tempFile = File.createTempFile("receipt", ".pdf");
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(pdfBytes);
        }

        helper.addAttachment("receipt.pdf", tempFile);
        mailSender.send(message);

        tempFile.delete();
    }
}
