package com.cinema.common.utils;

import com.cinema.common.base.ServiceResult;
import com.cinema.common.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
public class DateUtils {
    public static LocalDate convertStringToLocalDate(String date, String pattern) {
        try {
            return LocalDate.parse(date, DateTimeFormatter.ofPattern(pattern));
        } catch (Exception e) {
            log.error("[CONVERT_DATE] error: {}", e.getMessage());
            throw new RuntimeException(ErrorCode.TIME_INVALID.getMessage());
        }
    }

    public static String convertLocalDateTimeToString(LocalDateTime date, String pattern) {
        try {
            return DateTimeFormatter.ofPattern(pattern).format(date);
        } catch (Exception e) {
            log.error("[CONVERT_DATE_TO_STRING] error: {}", e.getMessage());
            throw new RuntimeException(ErrorCode.TIME_INVALID.getMessage());
        }
    }
}
