package com.example.authify.service;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.JavaMailSender;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

    @Value("${spring.mail.properties.mail.smtp.from}")
    private String fromEmail;

    public void sendWelcomeEmail(String toEmail,String name){
        SimpleMailMessage message=new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("Welcome to our platform");
        message.setText("Hello "+name+" ,\n\n Thanks for Joining us");
        mailSender.send(message);
    }

    public void sendResetOtp(String toEmail,String otp){
        SimpleMailMessage message=new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("Password reset otp");
        message.setText(" Here is your "+otp+" for resetting your password,will expire in 10 min");
        System.out.println("Sending...");
        mailSender.send(message);
        System.out.println("Sent!");
    }

    public void sendOtpEmail(String toEmail, String  otp){
        SimpleMailMessage message=new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("Account verification OTP");
        message.setText(" Here is your "+otp+" for Account Verification,will expire in 10 min");
        mailSender.send(message);
    }

}
