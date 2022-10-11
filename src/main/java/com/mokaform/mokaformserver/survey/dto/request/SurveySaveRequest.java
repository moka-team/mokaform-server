package com.mokaform.mokaformserver.survey.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
public class SurveySaveRequest {
    @NotNull
    private Long surveyor_id;

    @NotNull
    private String title;

    @NotNull
    private Boolean is_anonymous;

    @NotNull
    private Boolean is_public;

    @NotNull
    private String sharing_key;

    @NotNull
    private Boolean is_deleted;

    @Builder
    public SurveySaveRequest(Long surveyor_id, String title,
                  Boolean is_anonymous, Boolean is_public, Boolean is_deleted,
                  String sharing_key
                  ) {
        this.surveyor_id = surveyor_id;
        this.title = title;
        this.is_anonymous = is_anonymous;
        this.is_public = is_public;
        this.is_deleted = is_deleted;
        this.sharing_key = sharing_key;

    }

}
