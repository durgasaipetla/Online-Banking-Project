package com.banking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendRegistrationOtpEmail(String toEmail, String otp) {
        
    	// String bankName = "Your Bank Name"; // you can store in application.properties later
        String customerCareNumber = "1800-123-456"; 
        String bankEmail = "support@yourbank.com";
        String bankWebsite = "www.yourbank.com";

        String emailBody = 
                "Dear customer" + ",\n\n" +
                "Thank you for registering for Online Banking.\n\n" +
                "To complete your registration, please use the One-Time Password (OTP) provided below:\n\n" +
                "Your OTP: " + otp + "\n\n" +
                "(Valid for 10 minutes)\n\n" +
                "For your security:\n" +
                "- Do not share this OTP with anyone, including bank representatives.\n" +
                "- "  + "Bank will never ask you for your OTP, password, or PIN via phone, email, or SMS.\n\n" +
                "If you did not initiate this request, please contact our customer support immediately at " + customerCareNumber + " or visit your nearest branch.\n\n" +
                "Thank you for choosing our Bank " + ".\n\n" +
                "Sincerely,\n" +
                "Digital Banking Team\n" +
                bankEmail + "\n" +
                bankWebsite;
    
    	
    	SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Your One-Time Password (OTP) for Online Banking Registration");
        message.setText(emailBody);
        mailSender.send(message);
    }
    public void sendPasswordResetOtpEmail(String toEmail, String otp) {

        String customerCareNumber = "1800-123-456";
        String bankEmail = "support@yourbank.com";
        String bankWebsite = "www.yourbank.com";
        String emailBody =
                "Dear Customer,\n\n" +
                "We received a request to reset your online banking password.\n\n" +
                "Please use the One-Time Password (OTP) below to proceed with resetting your password:\n\n" +
                "Your OTP: " + otp + "\n\n" +
                "(Valid for 10 minutes)\n\n" +
                "For your security:\n" +
                "- Do not share this OTP with anyone.\n" +
                "- Our bank will never ask for your OTP, password, or PIN via phone, email, or SMS.\n\n" +
                "If you did not initiate this password reset, please contact our customer support immediately at " + customerCareNumber + " or visit your nearest branch.\n\n" +
                "Thank you for banking with us.\n\n" +
                "Sincerely,\n" +
                "Digital Banking Team\n" +
                bankEmail + "\n" +
                bankWebsite;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Password Reset OTP - Online Banking");
        message.setText(emailBody);

        mailSender.send(message);
        
    }

}

