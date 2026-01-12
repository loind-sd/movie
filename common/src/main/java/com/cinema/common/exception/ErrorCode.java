package com.cinema.common.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    OK("0", "SUCCESS"),
    INVALID_REQUEST("400", "Invalid request"),
    UNAUTHORIZED("401", "Unauthorized access"),
    FORBIDDEN("403", "Forbidden"),
    INTERNAL_SERVER_ERROR("500", "Internal server error"),
    NOT_FOUND("404", "Resource not found"),

    // common error codes
    TIME_INVALID("901", "Time value is invalid"),


    // user error codes
    ACCOUNT_LOCKED_TOO_MANY_ATTEMPTS("1001", "Account locked due to too many failed login attempts"),
    USER_NOT_FOUND("1002", "User not found"),
    INVALID_CREDENTIALS("1003", "Invalid username or password"),
    PASSWORD_EXPIRED("1004", "Password has expired"),
    INSUFFICIENT_PERMISSIONS("1005", "Insufficient permissions to access this resource"),
    REFRESH_TOKEN_INVALID("1006", "REFRESH_TOKEN_INVALID"),

    // movie error codes
    MOVIE_NOT_FOUND("1101", "Movie not found"),
    MOVIE_ALREADY_EXISTS("1102", "Movie already exists"),
    INVALID_MOVIE_DATA("1103", "Invalid movie data provided"),

    // genre error codes
    GENRE_NOT_FOUND("1201", "Genre not found"),
    GENRE_ALREADY_EXISTS("1202", "Genre already exists"),
    INVALID_GENRE_DATA("1203", "Invalid genre data provided"),

    // person error codes
    PERSON_NOT_FOUND("1301", "Person not found"),
    PERSON_ALREADY_EXISTS("1302", "Person already exists"),
    INVALID_PERSON_DATA("1303", "Invalid person data provided"),

    // room error codes
    ROOM_NOT_FOUND("1401", "Room not found"),
    ROOM_ALREADY_EXISTS("1402", "Room already exists"),
    INVALID_ROOM_DATA("1403", "Invalid room data provided"),

    // showtime error codes
    SHOWTIME_NOT_FOUND("1501", "Showtime not found"),
    SHOWTIME_ALREADY_EXISTS("1502", "Showtime already exists"),
    INVALID_SHOWTIME_DATA("1503", "Invalid showtime data provided"),
    SHOWTIME_CONFLICT("1504", "Showtime conflicts with existing schedule"),
    SHOWTIME_NOT_SUITABLE_ROOM("1505", "No available rooms for the requested showtime"),
    SHOWTIME_DUPLICATE_TIME_SLOT("1506", "Showtime has a duplicate time slot"),

    // booking error codes
    BOOKING_NOT_FOUND("1601", "Booking not found"),
    BOOKING_ALREADY_EXISTS("1602", "Booking already exists"),
    INVALID_BOOKING_DATA("1603", "Invalid booking data provided"),
    SEAT_ALREADY_BOOKED("1604", "Seat is already booked"),

    // payment error codes
    PAYMENT_NOT_FOUND("1701", "Payment not found"),
    PAYMENT_ALREADY_PAID("1702", "Payment already paid"),



    ;

    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
