package com.devsuperior.DSCommerce.tests;

import org.springframework.mail.SimpleMailMessage;

public class EmailFactory {

    public static SimpleMailMessage createdSendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("jonasflajo@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        return message;
    }
}
