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
    private Long surveyorId;

    @NotNull
    private String title;

    @NotNull
    private Boolean isAnonymous;

    @NotNull
    private Boolean isPublic;

    @NotNull
    private LocalDateTime startDate;

    @NotNull
    private LocalDateTime endDate;

    @Builder
    public SurveySaveRequest(Long surveyorId, String title,
                  Boolean isAnonymous, Boolean isPublic, LocalDateTime startDate, LocalDateTime endDate
                  ) {
        this.surveyorId = surveyorId;
        this.title = title;
        this.isAnonymous = isAnonymous;
        this.isPublic = isPublic;
        this.endDate = endDate;
        this.startDate = startDate;

    }

}
