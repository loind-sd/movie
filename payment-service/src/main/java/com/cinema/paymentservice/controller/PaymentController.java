package com.cinema.paymentservice.controller;

import com.cinema.common.base.ServiceResult;
import com.cinema.paymentservice.dto.request.PaymentCreateRequest;
import com.cinema.paymentservice.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payment")
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping()
    public ResponseEntity<ServiceResult> create(@RequestBody PaymentCreateRequest request) {
        ServiceResult result = paymentService.create(request);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/complete")
    public ResponseEntity<ServiceResult> completePayment(@RequestBody PaymentCreateRequest request) {
        ServiceResult result = paymentService.completePayment(request);
        return ResponseEntity.ok(result);
    }
}
