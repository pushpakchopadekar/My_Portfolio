package com.pushpak.portfolio.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    private final JavaMailSender mailSender;

    @Value("${app.notify.email}")
    private String notifyEmail;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public MailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    // Sends the site owner an email whenever someone submits the contact form.
    // If mail isn't configured yet (still has placeholder password), this
    // fails silently so the contact form still works and the message still
    // gets saved to the database either way.
    public void sendContactNotification(String name, String email, String message) {
        try {
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setFrom(fromEmail);
            mail.setTo(notifyEmail);
            mail.setSubject("New portfolio contact message from " + name);
            mail.setText("Name: " + name + "\nEmail: " + email + "\n\nMessage:\n" + message);
            mailSender.send(mail);
        } catch (Exception e) {
            System.out.println("Email notification failed (check spring.mail.password in application.properties): " + e.getMessage());
        }
    }
}
