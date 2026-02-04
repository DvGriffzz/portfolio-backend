package com.diogo.portfolio_backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class EmailService {

    private final RestTemplate restTemplate;
    private final String apiKey;
    private final String fromEmail;

    public EmailService(@Value("${resend.api_key}") String apiKey,
                        @Value("${resend.mail_from}") String fromEmail) {
        this.restTemplate = new RestTemplate();
        this.apiKey = apiKey;
        this.fromEmail = fromEmail;
    }

    public void sendEmail(String to, String subject, String text) {
        String url = "https://api.resend.com/emails";

        Map<String, Object> body = Map.of(
                "from", fromEmail,
                "to", to,
                "subject", subject,
                "text", text
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            restTemplate.postForEntity(url, request, String.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send email: " + e.getMessage(), e);
        }
    }
}
