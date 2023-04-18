package com.sydoruk.service;

import com.sydoruk.model.Invoice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailService {
     private final JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String adminEmail;

    @Autowired
    public EmailService (JavaMailSender mailSender){
        this.mailSender=  mailSender;
    }

    public void sendEmail(final String to, final String subject, final String text) {
        final SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(adminEmail);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }

    public void sendInvoiceEmail(final Invoice invoice) throws MessagingException {
        final MimeMessage message = mailSender.createMimeMessage();
        final MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(adminEmail);
        helper.setTo(invoice.getUser().getEmail());
        helper.setSubject("Invoice Details");

        final String htmlContent = "<h1>Invoice Details</h1>" +
                "<p>ID: " + invoice.getId() + "</p>" +
                "<p>Date: " + invoice.getDate() + "</p>" +
                "<p>User Name: " + invoice.getUser().getFirstName() + " " + invoice.getUser().getLastName() + "</p>" +
                "<p>User Email: " + invoice.getUser().getEmail() + "</p>" +
                "<p>Reserved Place Number: " + invoice.getReservedPlace().getId() + "</p>" +
                "<p>Car Number: " + invoice.getCarNumber() + "</p>" +
                "<p>Start Date: " + invoice.getStartDate() + "</p>" +
                "<p>End Date: " + invoice.getEndDate() + "</p>" +
                "<p>Total Price $: " + invoice.getTotalPrice() + "</p>";

        helper.setText(htmlContent, true);

        mailSender.send(message);
    }

}