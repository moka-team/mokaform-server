package com.mokaform.mokaformserver.user.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@Getter
public class SignupRequest {

    @NotBlank
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String nickname;

    @NotBlank
    private String ageGroup;

    @NotBlank
    private String gender;

    @NotBlank
    private String job;

    @Builder
    public SignupRequest(String email, String password,
                         String nickname, String ageGroup,
                         String gender, String job) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.ageGroup = ageGroup;
        this.gender = gender;
        this.job = job;
    }
}
