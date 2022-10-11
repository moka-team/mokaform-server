package com.mokaform.mokaformserver.survey.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
public class SurveySaveRequest {
    @NotBlank
    private String surveyor_id;

    @NotBlank
    private String title;

//    @NotBlank
//    private LocalDateTime start_date;
//
//    @NotBlank
//    private LocalDateTime end_date;
//
//    @NotBlank
//    private Boolean is_anonymous;
//
//    @NotBlank
//    private Boolean is_public;

    @NotBlank
    private String sharing_key;

//    @NotBlank
//    private Boolean is_deleted;

//    @NotBlank
//    private LocalDateTime created_at;
//
//    @NotBlank
//    private LocalDateTime updated_at;

    @Builder
    public SurveySaveRequest(String surveyor_id, String title,
                  String sharing_key
                  ) {
        this.surveyor_id = surveyor_id;
        this.title = title;
        this.sharing_key = sharing_key;

    }

}
