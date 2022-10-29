package com.mokaform.mokaformserver.common.exception.errorcode;

import org.springframework.http.HttpStatus;

public enum CommonErrorCode implements ErrorCode {
    INVALID_PARAMETER("C001", HttpStatus.BAD_REQUEST, "Invalid parameter included"),
    RESOURCE_NOT_FOUND("C002", HttpStatus.NOT_FOUND, "Resource not exists"),
    INTERNAL_SERVER_ERROR("C003", HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error"),
    INVALID_REQUEST("C004", HttpStatus.BAD_REQUEST, "Invalid request"),
    ACCESS_TOKEN_EXPIRED("C005", HttpStatus.FORBIDDEN, "Access token is expired"),
    ILLEGAL_TOKEN("C006", HttpStatus.FORBIDDEN, "Illegal token"),
    LOGGED_OUT_ACCESS_TOKEN("C007", HttpStatus.BAD_REQUEST, "This access token has been logged out.");

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;

    CommonErrorCode(String code, HttpStatus httpStatus, String message) {
        this.code = code;
        this.httpStatus = httpStatus;
        this.message = message;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return this.httpStatus;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
