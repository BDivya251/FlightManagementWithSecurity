package com.security.config;

import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class MailConfig {

    @Bean
    public JavaMailSender mailSender() {
        JavaMailSenderImpl mail = new JavaMailSenderImpl();
        mail.setHost("smtp.gmail.com");
        mail.setPort(587);

        mail.setUsername("divyabathini08@gmail.com");
        mail.setPassword("bupo xxhm agya hjok");

        Properties props = mail.getJavaMailProperties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        return mail;
    }
}
