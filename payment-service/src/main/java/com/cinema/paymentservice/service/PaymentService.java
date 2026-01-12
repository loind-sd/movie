package com.cinema.paymentservice.service;

import com.cinema.common.base.ServiceResult;
import com.cinema.paymentservice.dto.request.PaymentCreateRequest;

public interface PaymentService {
    ServiceResult create(PaymentCreateRequest request);

    // giả lập thôi
    ServiceResult completePayment(PaymentCreateRequest request);
}
