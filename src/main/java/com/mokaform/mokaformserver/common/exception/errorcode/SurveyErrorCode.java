package com.mokaform.mokaformserver.common.exception.errorcode;

import org.springframework.http.HttpStatus;

public enum SurveyErrorCode implements ErrorCode {

    INVALID_SORT_TYPE("S001", HttpStatus.BAD_REQUEST, "Invalid survey sort type"),
    ;

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;

    SurveyErrorCode(String code, HttpStatus httpStatus, String message) {
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