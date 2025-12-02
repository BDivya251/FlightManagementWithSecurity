package com.book.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import com.book.config.RabbitMQConfig;
import com.book.service.BookingEmailEvent;

@Service
public class EmailListener {

    @RabbitListener(queues = RabbitMQConfig.EMAIL_QUEUE)
    public void handleEmailEvent(BookingEmailEvent event) {
    	System.out.println(" Received event in listener");
        // Here you would call real EmailService / JavaMailSender
        System.out.println("Sending email to: " + event.getEmail());
        System.out.println("   PNR: " + event.getPnr());
        System.out.println("   Message: " + event.getMessage());
    }
}