package com.rental.houserental.service.impl;

import com.rental.houserental.template.MailTemplate;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

    public void sendApprovalEmail(String toEmail, String title, String ownerName, String status) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(toEmail);
            helper.setSubject("Property " + status);
            String htmlContent;
            if (status == "APPROVED") {
                htmlContent = MailTemplate.buildApprovalTemplate(ownerName, title);
            } else {
                htmlContent = MailTemplate.buildRejectedTemplate(ownerName, title);
            }
            helper.setText(htmlContent, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }

    public void sendResetMail(String name, String toEmail, String token) {
        String resetLink = "http://localhost:5173/password/reset/" + token;
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(toEmail);
            helper.setSubject("Reset Your password");
            String htmlContent = MailTemplate.buildResetLink(name, resetLink);
            helper.setText(htmlContent, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public void sendBookingAcceptMail(String tenantName, String toEmail, String ownerName, String propertyTitle) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(toEmail);
            helper.setSubject("Reset Your password");
            String htmlContent = MailTemplate.buildAcceptBookingTemplate(tenantName, ownerName, propertyTitle);
            helper.setText(htmlContent, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public void sendBookingRejectMail(String tenantName, String toEmail, String ownerName, String propertyTitle) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(toEmail);
            helper.setSubject("Reset Your password");
            String htmlContent = MailTemplate.buildRejectBookingTemplate(tenantName, ownerName, propertyTitle);
            helper.setText(htmlContent, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public void sendEmailOtp(String toEmail, String otp){
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(toEmail);
            helper.setSubject("Verification OTP");
            String htmlContent = MailTemplate.buildOtpEmailTemplate(otp);
            helper.setText(htmlContent, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

}
