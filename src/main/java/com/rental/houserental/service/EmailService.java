package com.rental.houserental.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;
    public void sendOwnerApprovalEmail(String toEmail) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Owner Account Approved");

        message.setText(
                "Hello,\n\n" +
                        "Congratulations! Your Owner account has been approved by Admin.\n\n" +
                        "You can now login and list your rental properties.\n\n" +
                        "Thank you,\n" +
                        "House Rental Management System"
        );

        mailSender.send(message);
    }
}
