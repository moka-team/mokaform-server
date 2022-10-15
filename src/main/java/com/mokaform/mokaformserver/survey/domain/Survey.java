package com.mokaform.mokaformserver.survey.domain;

import com.mokaform.mokaformserver.common.entitiy.BaseEntity;
import com.mokaform.mokaformserver.user.domain.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "survey")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Survey extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "survey_id")
    private Long surveyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "surveyor_id", referencedColumnName = "user_id", nullable = false)
    private User user;

    @Column(name = "title", nullable = false, length = 50)
    private String title;

    @Column(name = "summary", nullable = false)
    private String summary;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "is_anonymous", nullable = false)
    private Boolean isAnonymous;

    @Column(name = "is_public", nullable = false)
    private Boolean isPublic;

    @Column(name = "sharing_key", nullable = false, length = 10)
    private String sharingKey;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;

    @OneToMany(mappedBy = "survey")
    private List<Question> questions = new ArrayList<>();

    @Builder
    public Survey(User user, String title,
                  String summary, LocalDate startDate, LocalDate endDate,
                  Boolean isAnonymous, Boolean isPublic) {
        this.user = user;
        this.title = title;
        this.summary = summary;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isAnonymous = isAnonymous;
        this.isPublic = isPublic;
        this.sharingKey = RandomStringUtils.random(10, true, true);
        this.isDeleted = false;
    }
}
