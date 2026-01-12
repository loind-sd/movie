package com.cinema.common.exception;

import com.cinema.common.base.ServiceResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ServiceResult> handleBadRequest(BadRequestException ex) {
        ServiceResult result = ServiceResult.fail(ex.getErrorCode());
        ex.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
    }

    // Catch lỗi chưa xử lý
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ServiceResult> handleException(Exception ex) {
        ServiceResult result = ServiceResult.fails(HttpStatus.INTERNAL_SERVER_ERROR.name(), ex.getMessage());
        ex.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
    }
}
