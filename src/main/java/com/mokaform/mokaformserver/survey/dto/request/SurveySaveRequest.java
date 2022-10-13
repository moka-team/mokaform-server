package com.mokaform.mokaformserver.survey.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

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
    private LocalDateTime startDate;

    @NotNull
    private LocalDateTime endDate;

    @Builder
    public SurveySaveRequest(Long surveyor_id, String title,
                  Boolean is_anonymous, Boolean is_public, LocalDateTime endDate, LocalDateTime startDate
                  ) {
        this.surveyor_id = surveyor_id;
        this.title = title;
        this.is_anonymous = is_anonymous;
        this.is_public = is_public;
        this.endDate = endDate;
        this.startDate = startDate;
    }

}
