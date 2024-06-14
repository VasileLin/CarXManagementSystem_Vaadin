package com.vmoon.carx.utils;

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

    public void sendEmailWithAttachment(String to, byte[] pdfBytes) throws MessagingException, IOException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom("vmoon.apps@gmail.com");
        helper.setTo(to);
        helper.setSubject("Your Receipt in electronic format");
        helper.setText("Please find your receipt attached.");

        File tempFile = File.createTempFile("receipt", ".pdf");
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(pdfBytes);
        }

        helper.addAttachment("receipt.pdf", tempFile);
        mailSender.send(message);

        tempFile.delete();
    }
}
