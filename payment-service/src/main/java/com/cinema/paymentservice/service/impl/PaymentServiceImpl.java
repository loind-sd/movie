package com.cinema.paymentservice.service.impl;

import com.cinema.common.base.ServiceResult;
import com.cinema.common.constants.CommonConstants;
import com.cinema.common.dto.kafkaMessage.BookingCompleteMessage;
import com.cinema.common.enums.PaymentStatus;
import com.cinema.common.exception.BadRequestException;
import com.cinema.common.exception.ErrorCode;
import com.cinema.kafka.procedure.KafkaProducer;
import com.cinema.paymentservice.dto.request.PaymentCreateRequest;
import com.cinema.paymentservice.entity.Payment;
import com.cinema.paymentservice.repository.PaymentRepository;
import com.cinema.paymentservice.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final KafkaProducer kafkaProducer;

    @Override
    public ServiceResult create(PaymentCreateRequest request) {
        log.debug("Create PaymentCreateRequest");
        Payment payment = Payment.builder()
                .bookingId(request.getBookingId())
                .totalPrice(request.getTotalPrice())
                .status(PaymentStatus.HOLDING)
                .build();
        paymentRepository.save(payment);
        return ServiceResult.ok();
    }

    @Override
    public ServiceResult completePayment(PaymentCreateRequest request) {
        log.debug("Complete PaymentCreateRequest");
        Optional<Payment> paymentOptional = paymentRepository.findByBookingId(request.getBookingId());
        if (paymentOptional.isEmpty()) {
            throw new BadRequestException(ErrorCode.PAYMENT_NOT_FOUND);
        }

        Payment payment = paymentOptional.get();
        if (Objects.equals(payment.getStatus(), PaymentStatus.SUCCESS)) {
            throw new BadRequestException(ErrorCode.PAYMENT_ALREADY_PAID);
        }

        payment.setStatus(PaymentStatus.SUCCESS);

        // gọi qua booking-service để hoàn thành booking
        kafkaProducer.send(CommonConstants.KafkaTopic.BOOKING_COMPLETE, new BookingCompleteMessage(request.getBookingId(), payment.getTotalPrice()));
        // gọi qua notification-service để gửi mail

        return ServiceResult.ok();
    }
}
