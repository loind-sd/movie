package com.example.aws_lambda.controller;

import com.example.aws_lambda.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/email")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @GetMapping("/test")
    public String sendTest() {

        emailService.sendEmail(
                "loindfpt2k@gmail.com",
                "Test SES",
                "<h1>Hello from Spring Boot + SES 🚀</h1>"
        );

        return "Email sent!";
    }
}
