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
    private Long surveyorId;

    @NotNull
    private String title;

    @NotNull
    private Boolean isAnonymous;

    @NotNull
    private Boolean isPublic;

    @NotNull
    private String sharingKey;

    @Builder
    public SurveySaveRequest(Long surveyorId, String title,
                  Boolean isAnonymous, Boolean isPublic,
                  String sharingKey
                  ) {
        this.surveyorId = surveyorId;
        this.title = title;
        this.isAnonymous = isAnonymous;
        this.isPublic = isPublic;
        this.sharingKey = sharingKey;

    }

}
