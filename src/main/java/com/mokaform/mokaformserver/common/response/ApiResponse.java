package com.mokaform.mokaformserver.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private String message;
    private T data;

    @Builder
    public ApiResponse(String message, T data) {
        this.message = message;
        this.data = data;
    }
}
