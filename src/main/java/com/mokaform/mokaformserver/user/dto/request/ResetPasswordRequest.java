package com.mokaform.mokaformserver.user.dto.request;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class ResetPasswordRequest {

    @NotBlank
    private String email;

    @NotBlank
    private String password;

    public ResetPasswordRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
