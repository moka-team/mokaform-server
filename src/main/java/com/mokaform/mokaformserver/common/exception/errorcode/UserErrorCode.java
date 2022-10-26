package com.mokaform.mokaformserver.common.exception.errorcode;

import org.springframework.http.HttpStatus;

public enum UserErrorCode implements ErrorCode {
    USER_NOT_FOUND("U001", HttpStatus.NOT_FOUND, "User not found"),
    INVALID_ACCOUNT_REQUEST("U002", HttpStatus.BAD_REQUEST, "아이디 및 비밀번호가 올바르지 않습니다.");

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;

    UserErrorCode(String code, HttpStatus httpStatus, String message) {
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
