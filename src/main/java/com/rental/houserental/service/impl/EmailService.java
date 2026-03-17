package com.rental.houserental.service.impl;

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

    //for sending the approval email
    public void sendPropertyApprovalEmail(String toEmail, String propertyTitle) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Property Approved");

        message.setText(
                "Dear Owner,\n\n" +
                        "Your property \"" + propertyTitle + "\" has been approved by the admin.\n" +
                        "It is now visible to tenants in the system.\n\n" +
                        "Thank you for listing with us.\n" +
                        "House Rental Team"
        );

        mailSender.send(message);
    }
}
