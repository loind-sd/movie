package com.cinema.common.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    OK("0", "SUCCES"),
    INVALID_REQUEST("400", "Invalid request"),
    UNAUTHORIZED("401", "Unauthorized access"),
    FORBIDDEN("403", "Forbidden"),
    INTERNAL_SERVER_ERROR("500", "Internal server error"),
    NOT_FOUND("404", "Resource not found"),


    // user error codes
    ACCOUNT_LOCKED_TOO_MANY_ATTEMPTS("1001", "Account locked due to too many failed login attempts"),
    USER_NOT_FOUND("1002", "User not found"),
    INVALID_CREDENTIALS("1003", "Invalid username or password"),
    PASSWORD_EXPIRED("1004", "Password has expired"),
    INSUFFICIENT_PERMISSIONS("1005", "Insufficient permissions to access this resource"),

    // movie error codes
    MOVIE_NOT_FOUND("2001", "Movie not found"),
    MOVIE_ALREADY_EXISTS("2002", "Movie already exists"),
    INVALID_MOVIE_DATA("2003", "Invalid movie data provided"),


    ;

    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
