package com.example.authify.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final RestTemplate restTemplate = new RestTemplate();

    private static final String BREVO_API_URL = "https://api.brevo.com/v3/smtp/email";

    @Value("${spring.mail.properties.mail.smtp.from}")
    private String fromEmail;

    @Value("${brevo.api.key}")
    private String brevoApiKey;

    private void sendEmail(String toEmail, String subject, String content) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("api-key", brevoApiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = new HashMap<>();
        body.put("sender", Map.of("email", fromEmail, "name", "Authify"));
        body.put("to", List.of(Map.of("email", toEmail)));
        body.put("subject", subject);
        body.put("htmlContent", content);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            restTemplate.postForEntity(BREVO_API_URL, request, String.class);
        } catch (org.springframework.web.client.HttpClientErrorException e) {
            System.out.println("Brevo error response: " + e.getResponseBodyAsString());
            throw new RuntimeException("Unable to send Email: " + e.getResponseBodyAsString(), e);
        } catch (Exception e) {
            throw new RuntimeException("Unable to send Email", e);
        }
    }

    public void sendWelcomeEmail(String toEmail, String name) {
        String content = "<p>Hello " + name + ",</p><p>Thanks for joining us</p>";
        sendEmail(toEmail, "Welcome to our platform", content);
    }

    public void sendResetOtp(String toEmail, String otp) {
        String content = "<p>Here is your OTP: <b>" + otp + "</b> for resetting your password, will expire in 10 min</p>";
        System.out.println("Sending...");
        sendEmail(toEmail, "Password reset OTP", content);
        System.out.println("Sent!");
    }

    public void sendOtpEmail(String toEmail, String otp) {
        String content = "<p>Here is your OTP: <b>" + otp + "</b> for Account Verification, will expire in 10 min</p>";
        sendEmail(toEmail, "Account verification OTP", content);
    }
}