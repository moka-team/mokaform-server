package com.mokaform.mokaformserver.user.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mokaform.mokaformserver.user.domain.User;
import com.mokaform.mokaformserver.user.domain.enums.AgeGroup;
import com.mokaform.mokaformserver.user.domain.enums.Gender;
import com.mokaform.mokaformserver.user.domain.enums.Job;
import lombok.Getter;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(NON_NULL)
@Getter
public class LoginResponse {

    private Long id;

    private String email;

    private String nickname;

    private AgeGroup ageGroup;

    private Gender gender;

    private Job job;

    private String profile_image;

    public LoginResponse(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.nickname = user.getNickname();
        this.ageGroup = user.getAgeGroup();
        this.gender = user.getGender();
        this.job = user.getJob();
        this.profile_image = user.getProfile_image();
    }
}
