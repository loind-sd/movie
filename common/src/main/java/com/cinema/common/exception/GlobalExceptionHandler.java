package com.cinema.common.exception;

import com.cinema.common.base.ServiceResult;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.HandlerMapping;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    private final MeterRegistry meterRegistry;
    private final String applicationName;

    public GlobalExceptionHandler(
            @Autowired(required = false) MeterRegistry meterRegistry,
            @Value("${spring.application.name:unknown-service}") String applicationName
    ) {
        this.meterRegistry = meterRegistry;
        this.applicationName = applicationName;
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ServiceResult> handleBadRequest(BadRequestException ex, HttpServletRequest request) {
        countException(ex, request, ex.getErrorCode());

        ServiceResult result = ServiceResult.fail(ex.getErrorCode());
        ex.printStackTrace();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

    // Catch lỗi chưa xử lý
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ServiceResult> handleException(Exception ex, HttpServletRequest request) {
        countException(ex, request, ErrorCode.INTERNAL_SERVER_ERROR);

        ServiceResult result = ServiceResult.fails(HttpStatus.INTERNAL_SERVER_ERROR.name(), ex.getMessage());
        ex.printStackTrace();

        StringBuilder errorMessage = new StringBuilder();
        for (StackTraceElement item : ex.getStackTrace()) {
            errorMessage.append(item.toString()).append("\n");
        }

        log.error("[API_ERROR] {} - {}", request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE), errorMessage);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
    }

    private void countException(
            Exception ex,
            HttpServletRequest request,
            ErrorCode errorCode
    ) {
        if (meterRegistry != null) {
            Counter.builder("service_exceptions_total")
                    .description("Total exceptions by service")
                    .tag("application", applicationName)
                    .tag("uri", (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE))
                    .tag("exception", ex.getClass().getSimpleName())
                    .tag("error_code", errorCode.getCode())
                    .register(meterRegistry)
                    .increment();
        }
    }
}
