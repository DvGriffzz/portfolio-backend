package com.diogo.portfolio_backend.controller;

import com.diogo.portfolio_backend.service.EmailService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class EmailController {
    private final EmailService emailService;
    public EmailController(EmailService emailService){
        this.emailService = emailService;
    }

    @PostMapping("/send-email")
    public ResponseEntity<String> sendEmail(
            @RequestParam String to,
            @RequestParam String subject,
            @RequestParam String text,
            @AuthenticationPrincipal UserDetails userDetails // inject current user
    ) throws IOException {
        emailService.sendEmail(to, subject, text);
        return ResponseEntity.ok("Email sent to " + to + " by user " + userDetails.getUsername());
    }
}
