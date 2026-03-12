package com.example.aws_lambda.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.*;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final SesClient sesClient;

    @Value("${aws.sender-email}")
    private String sender;

    public void sendEmail(String to, String subject, String content) {

        SendEmailRequest request = SendEmailRequest.builder()
                .destination(Destination.builder()
                        .toAddresses(to)
                        .build())
                .message(Message.builder()
                        .subject(Content.builder().data(subject).build())
                        .body(Body.builder()
                                .html(Content.builder().data(content).build())
                                .build())
                        .build())
                .source(sender)
                .build();

        SendEmailResponse response = sesClient.sendEmail(request);
        System.out.println(response);
    }
}
