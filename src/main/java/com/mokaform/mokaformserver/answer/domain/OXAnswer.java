package com.mokaform.mokaformserver.answer.domain;

import com.mokaform.mokaformserver.common.entitiy.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "ox_answer")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class OXAnswer extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ox_answer_id")
    private Long oxAnswerId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "answer_id", referencedColumnName = "answer_id", nullable = false)
    private Answer answer;

    @Column(name = "is_yes", nullable = false)
    private Boolean isYes;

    @Builder
    public OXAnswer(Answer answer, Boolean isYes) {
        this.answer = answer;
        this.isYes = isYes;
    }
}
