package com.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
	@Autowired
    private JavaMailSender mailSender;

    public void sendResetEmail(String toEmail, String link) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Reset Your Password");
        message.setText("Click to reset password:\n" + link);
        mailSender.send(message);
        System.out.println("Email sended");
    }
}
